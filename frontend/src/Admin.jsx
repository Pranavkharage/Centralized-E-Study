import React, { useEffect, useState } from 'react'
import { Plus, Pencil, Trash2, Users, BookOpen, Layers, ClipboardCheck, ShieldCheck } from 'lucide-react'
import { api } from './api.js'
import { displayName } from './constants.js'

export default function Admin({ me }) {
  const [tab, setTab] = useState('overview')
  return (
    <>
      <section className="page-heading">
        <p className="eyebrow">ADMIN</p><h1>Admin panel</h1>
        <p className="muted">Manage study content and users.</p>
      </section>
      <div className="tabs">
        <button className={tab === 'overview' ? 'active' : ''} onClick={() => setTab('overview')}>Overview</button>
        <button className={tab === 'subjects' ? 'active' : ''} onClick={() => setTab('subjects')}>Subjects &amp; topics</button>
        <button className={tab === 'users' ? 'active' : ''} onClick={() => setTab('users')}>Users</button>
      </div>
      {tab === 'overview' && <Overview />}
      {tab === 'subjects' && <SubjectsAdmin />}
      {tab === 'users' && <UsersAdmin me={me} />}
    </>
  )
}

function Overview() {
  const [s, setS] = useState(null)
  const [err, setErr] = useState('')
  useEffect(() => { api('/api/admin/stats').then(r => setS(r.data)).catch(e => setErr(e.message)) }, [])
  if (err) return <p className="notice">{err}</p>
  if (!s) return <p className="muted">Loading…</p>
  const cards = [
    [Users, s.users, 'Total users'], [ShieldCheck, s.admins, 'Admins'], [BookOpen, s.subjects, 'Subjects'],
    [Layers, s.topics, 'Topics'], [ClipboardCheck, s.quizzesTaken, 'Quizzes taken']
  ]
  return <div className="admin-stats">{cards.map(([Icon, n, label]) => (
    <div className="stat with-icon" key={label}><Icon size={20} /><div><b>{n}</b><span>{label}</span></div></div>))}</div>
}

const blankTopic = { topicName: '', theory: '', example: '', code: '' }

function SubjectsAdmin() {
  const [names, setNames] = useState([])
  const [current, setCurrent] = useState(null)
  const [newName, setNewName] = useState('')
  const [editor, setEditor] = useState(null)          // null | { index, ...topic }
  const [qa, setQa] = useState({ question: '', answer: '' })
  const [msg, setMsg] = useState({ text: '', ok: false })

  const loadNames = () => api('/api/subjects/all').then(r => setNames(r.data || []))
  useEffect(() => { loadNames().catch(e => setMsg({ text: e.message })) }, [])

  // runs an admin action and shows its success/failure message
  async function run(action, okText) {
    setMsg({ text: '' })
    try { await action(); if (okText) setMsg({ text: okText, ok: true }) } catch (e) { setMsg({ text: e.message, ok: false }) }
  }
  const post = (path, body, method = 'POST') => api(path, { method, body: JSON.stringify(body) })
  const base = () => `/api/admin/subjects/${encodeURIComponent(current.subject)}`

  const select = name => run(async () => { setCurrent((await api(`/api/subjects/${encodeURIComponent(name)}`)).data); setEditor(null) })
  const create = e => { e.preventDefault(); run(async () => {
    const r = await post('/api/admin/subjects', { name: newName }); setNewName(''); await loadNames(); setCurrent(r.data)
  }, 'Subject created') }
  const removeSubject = () => { if (window.confirm(`Delete subject ${current.subject} and all its topics?`)) run(async () => {
    await api(base(), { method: 'DELETE' }); setCurrent(null); await loadNames()
  }, 'Subject deleted') }
  const saveTopic = e => { e.preventDefault(); const { index, ...t } = editor; run(async () => {
    const r = index === -1 ? await post(`${base()}/topics`, t) : await post(`${base()}/topics/${index}`, t, 'PUT')
    setCurrent(r.data); setEditor(null)
  }, 'Topic saved (its quiz will be regenerated)') }
  const removeTopic = i => { if (window.confirm('Delete this topic?')) run(async () => {
    setCurrent((await api(`${base()}/topics/${i}`, { method: 'DELETE' })).data)
  }, 'Topic deleted') }
  const addQa = e => { e.preventDefault(); run(async () => { setCurrent((await post(`${base()}/questions`, qa)).data); setQa({ question: '', answer: '' }) }, 'Question added') }
  const removeQa = i => run(async () => { setCurrent((await api(`${base()}/questions/${i}`, { method: 'DELETE' })).data) }, 'Question deleted')
  const setField = k => e => setEditor(v => ({ ...v, [k]: e.target.value }))

  return (
    <div className="admin-grid">
      <aside className="panel list-card">
        <h3>Subjects</h3>
        {names.map(n => <button key={n} className={'list-item' + (current?.subject === n ? ' active' : '')} onClick={() => select(n)}>{n}</button>)}
        <form onSubmit={create} className="inline-form">
          <input value={newName} onChange={e => setNewName(e.target.value)} placeholder="New subject, e.g. JAVA" required />
          <button className="primary small" title="Add subject"><Plus size={16} /></button>
        </form>
      </aside>

      <section>
        {msg.text && <p className={'notice' + (msg.ok ? ' ok' : '')}>{msg.text}</p>}
        {!current ? <p className="muted">Select a subject to manage its topics and interview questions.</p> : <>
          <div className="panel">
            <div className="row-between">
              <h3>{current.subject} · topics ({current.topics?.length || 0})</h3>
              <div className="row-actions">
                <button className="primary small" onClick={() => setEditor({ index: -1, ...blankTopic })}><Plus size={15} /> Add topic</button>
                <button className="outline small danger" onClick={removeSubject}><Trash2 size={15} /> Delete subject</button>
              </div>
            </div>
            {(current.topics || []).map((t, i) => (
              <div className="row-item" key={i}>
                <span>{String(i + 1).padStart(2, '0')}</span><b>{t.topicName}</b>
                <div className="row-actions">
                  <button className="outline small" onClick={() => setEditor({ index: i, topicName: t.topicName, theory: t.theory || '', example: t.example || '', code: t.code || '' })}><Pencil size={14} /> Edit</button>
                  <button className="outline small danger" onClick={() => removeTopic(i)}><Trash2 size={14} /></button>
                </div>
              </div>
            ))}
            {!(current.topics || []).length && <p className="muted">No topics yet.</p>}
          </div>

          {editor && (
            <form className="panel" onSubmit={saveTopic}>
              <h3>{editor.index === -1 ? 'New topic' : 'Edit topic'}</h3>
              <label className="field"><span>Topic name</span><input value={editor.topicName} onChange={setField('topicName')} required /></label>
              <label className="field"><span>Concept (theory)</span><textarea rows={5} value={editor.theory} onChange={setField('theory')} required /></label>
              <label className="field"><span>Example</span><textarea rows={2} value={editor.example} onChange={setField('example')} /></label>
              <label className="field"><span>Code example</span><textarea rows={6} className="mono" value={editor.code} onChange={setField('code')} /></label>
              <div className="form-actions"><button className="primary">Save topic</button><button type="button" className="outline" onClick={() => setEditor(null)}>Cancel</button></div>
            </form>
          )}

          <div className="panel">
            <h3>Interview Q&amp;A ({current.questions?.length || 0})</h3>
            {(current.questions || []).map((q, i) => (
              <div className="row-item" key={i}><span>Q{i + 1}</span><b>{q.question}</b>
                <div className="row-actions"><button className="outline small danger" onClick={() => removeQa(i)}><Trash2 size={14} /></button></div></div>
            ))}
            <form onSubmit={addQa}>
              <label className="field"><span>Question</span><input value={qa.question} onChange={e => setQa({ ...qa, question: e.target.value })} required /></label>
              <label className="field"><span>Answer</span><textarea rows={3} value={qa.answer} onChange={e => setQa({ ...qa, answer: e.target.value })} required /></label>
              <button className="primary small"><Plus size={15} /> Add question</button>
            </form>
          </div>
        </>}
      </section>
    </div>
  )
}

function UsersAdmin({ me }) {
  const [users, setUsers] = useState([])
  const [msg, setMsg] = useState({ text: '', ok: false })
  useEffect(() => { api('/api/admin/users').then(r => setUsers(r.data || [])).catch(e => setMsg({ text: e.message })) }, [])

  async function changeRole(u, role) {
    setMsg({ text: '' })
    try {
      const r = await api(`/api/admin/users/${u.id}/role`, { method: 'PUT', body: JSON.stringify({ role }) })
      setUsers(list => list.map(x => (x.id === u.id ? r.data : x)))
      setMsg({ text: `${displayName(u)} is now ${role === 'ADMIN' ? 'an admin' : 'a student'}`, ok: true })
    } catch (e) { setMsg({ text: e.message, ok: false }) }
  }

  return (
    <>
      {msg.text && <p className={'notice' + (msg.ok ? ' ok' : '')}>{msg.text}</p>}
      <div className="table-wrap"><table className="results">
        <thead><tr><th>Name</th><th>Username</th><th>Email</th><th>Phone</th><th>Branch</th><th>Year</th><th>Role</th></tr></thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{[u.firstName, u.lastName].filter(Boolean).join(' ') || '—'}</td><td>{u.username}</td><td>{u.email || '—'}</td>
              <td>{u.phone || '—'}</td><td>{u.branch || '—'}</td><td>{u.studyYear || '—'}</td>
              <td><select className="role-select" value={u.role} disabled={u.username === me.username} onChange={e => changeRole(u, e.target.value)}>
                <option>STUDENT</option><option>ADMIN</option></select></td>
            </tr>
          ))}
        </tbody>
      </table></div>
    </>
  )
}
