import React, { useState } from 'react'
import { api } from './api.js'
import { BRANCHES, YEARS, displayName, initials } from './constants.js'

export function Avatar({ user, size = 38 }) {
  return <span className="avatar" style={{ width: size, height: size, fontSize: size * 0.38 }}>{initials(user)}</span>
}

export function ProgressBar({ sp }) {
  if (!sp) return null
  const pct = sp.totalTopics ? Math.round((100 * sp.completedTopics) / sp.totalTopics) : 0
  return (
    <div className="pbar-wrap">
      <div className="pbar"><i style={{ width: pct + '%' }} /></div>
      <small>{sp.completedTopics}/{sp.totalTopics} topics studied</small>
    </div>
  )
}

export function StatsStrip({ summary }) {
  if (!summary) return null
  return (
    <div className="stats-strip">
      <div className="stat"><b>{summary.percent}%</b><span>Overall progress</span></div>
      <div className="stat"><b>{summary.completedTopics}/{summary.totalTopics}</b><span>Topics studied</span></div>
      <div className="stat"><b>{summary.attempts}</b><span>Quizzes taken</span></div>
      <div className="stat"><b>{summary.attempts ? summary.averagePercent + '%' : '—'}</b><span>Average quiz score</span></div>
    </div>
  )
}

// Study progress + quiz results (this is the page that used to be called "Profile & progress")
export function ProgressPage({ user, summary }) {
  return (
    <>
      <section className="page-heading">
        <p className="eyebrow">MY PROGRESS</p>
        <h1>Study progress &amp; quiz results</h1>
      </section>
      <div className="profile-card">
        <Avatar user={user} size={52} />
        <div><b>{displayName(user)}</b><span className="muted">{user.role === 'ADMIN' ? 'Administrator' : 'Student'}</span></div>
        {summary && summary.attempts > 0 && <div className="best">Best quiz score <b>{summary.bestPercent}%</b></div>}
      </div>
      <StatsStrip summary={summary} />
      <div className="section-title"><div><h2>Progress by subject</h2></div></div>
      <div className="subject-progress">
        {(summary?.subjects || []).map(sp => (
          <div className="sp-row" key={sp.subject}><b>{sp.subject}</b><ProgressBar sp={sp} /></div>
        ))}
      </div>
      <div className="section-title"><div><h2>Recent quiz results</h2></div></div>
      {!summary || summary.recentAttempts.length === 0
        ? <p className="muted">No quizzes yet. Open any topic and click “Take AI quiz”.</p>
        : <div className="table-wrap"><table className="results">
            <thead><tr><th>Subject</th><th>Topic</th><th>Score</th><th>Date</th></tr></thead>
            <tbody>
              {summary.recentAttempts.map(a => {
                const pct = a.total ? Math.round((100 * a.score) / a.total) : 0
                return (
                  <tr key={a.id}>
                    <td>{a.subject}</td><td>{a.topicName}</td>
                    <td><span className={'score ' + (pct >= 70 ? 'good' : pct >= 40 ? 'mid' : 'low')}>{a.score}/{a.total} · {pct}%</span></td>
                    <td>{a.submittedAt ? new Date(a.submittedAt).toLocaleDateString() : ''}</td>
                  </tr>
                )
              })}
            </tbody>
          </table></div>}
    </>
  )
}

// User details page, opened from the avatar in the top bar
export function AccountPage({ user, onSaved }) {
  const [f, setF] = useState({
    firstName: user.firstName || '', lastName: user.lastName || '', email: user.email || '',
    phone: user.phone || '', branch: user.branch || '', studyYear: user.studyYear || ''
  })
  const [msg, setMsg] = useState({ text: '', ok: false })
  const [busy, setBusy] = useState(false)
  const set = k => e => setF(v => ({ ...v, [k]: e.target.value }))

  async function save(e) {
    e.preventDefault(); setBusy(true); setMsg({ text: '' })
    try {
      const r = await api('/api/users/me', { method: 'PUT', body: JSON.stringify(f) })
      onSaved(r.data); setMsg({ text: 'Profile saved', ok: true })
    } catch (err) { setMsg({ text: err.message, ok: false }) } finally { setBusy(false) }
  }

  return (
    <>
      <section className="page-heading"><p className="eyebrow">ACCOUNT</p><h1>My profile</h1></section>
      <div className="profile-card">
        <Avatar user={user} size={64} />
        <div><b>{displayName(user)}</b><span className="muted">@{user.username} · {user.role === 'ADMIN' ? 'Administrator' : 'Student'}</span></div>
      </div>
      <form className="panel" onSubmit={save}>
        <div className="form-grid">
          <label className="field"><span>First name</span><input value={f.firstName} onChange={set('firstName')} required /></label>
          <label className="field"><span>Last name (surname)</span><input value={f.lastName} onChange={set('lastName')} required /></label>
          <label className="field"><span>Username</span><input value={user.username} disabled /></label>
          <label className="field"><span>Email</span><input type="email" value={f.email} onChange={set('email')} /></label>
          <label className="field"><span>Phone number</span><input type="tel" value={f.phone} onChange={set('phone')} /></label>
          <label className="field"><span>Branch</span>
            <select value={f.branch} onChange={set('branch')}><option value="">Select branch</option>{BRANCHES.map(b => <option key={b}>{b}</option>)}</select></label>
          <label className="field"><span>Year of study</span>
            <select value={f.studyYear} onChange={set('studyYear')}><option value="">Select year</option>{YEARS.map(y => <option key={y}>{y}</option>)}</select></label>
        </div>
        <div className="form-actions">
          <button className="primary" disabled={busy}>{busy ? 'Saving…' : 'Save changes'}</button>
          {msg.text && <span className={'inline-msg' + (msg.ok ? ' ok' : '')}>{msg.text}</span>}
        </div>
      </form>
    </>
  )
}
