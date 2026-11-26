import React, { useEffect, useState } from 'react'
import { createRoot } from 'react-dom/client'
import { BookOpen, Brain, ChevronLeft, CircleUserRound, Code2, GraduationCap, LogOut, PlayCircle, Search, ShieldCheck, Check, Sparkles } from 'lucide-react'
import './style.css'
import { api } from './api.js'
import { ProgressBar, StatsStrip, Profile } from './Components.jsx'
import Quiz from './Quiz.jsx'

function App() {
  const [user, setUser] = useState(() => { try { return JSON.parse(sessionStorage.getItem('user')) } catch { return null } })
  const [mode, setMode] = useState('login')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState('')
  const [busy, setBusy] = useState(false)
  const [subjects, setSubjects] = useState([])
  const [selected, setSelected] = useState(null)
  const [query, setQuery] = useState('')
  const [tab, setTab] = useState('topics')
  const [topicIndex, setTopicIndex] = useState(0)
  const [view, setView] = useState('subjects')
  const [summary, setSummary] = useState(null)
  const [quizTopic, setQuizTopic] = useState(null)
  const loadSummary = () => api('/api/progress/summary').then(r => setSummary(r.data)).catch(() => {})
  const progressOf = name => summary?.subjects?.find(x => x.subject === name)
  const isDone = (sub, topic) => !!progressOf(sub)?.completed?.includes(topic)
  useEffect(() => { if (user) loadSummary() }, [user, selected, view, quizTopic])
  async function markStudied() {
    const t = selected.topics[topicIndex]
    try { await api('/api/progress/topic', { method: 'POST', body: JSON.stringify({ subject: selected.subject, topicName: t.topicName }) }); loadSummary() }
    catch (e) { alert(e.message) }
  }

  useEffect(() => { if (user && !selected) api('/api/subjects/all').then(r => setSubjects(r.data || [])).catch(e => setMessage(e.message)) }, [user, selected])

  async function submit(e) {
    e.preventDefault(); setMessage(''); setBusy(true)
    try {
      const result = await api(`/api/auth/${mode}`, { method: 'POST', body: JSON.stringify({ username, password }) })
      if (mode === 'register') { setMessage('Account created. Please log in.'); setMode('login'); setPassword('') }
      else {
        sessionStorage.setItem('token', result.data.accessToken)
        setUser(result.data)
        sessionStorage.setItem('user', JSON.stringify(result.data))
      }
    } catch (err) { setMessage(err.message) } finally { setBusy(false) }
  }
  async function openSubject(name) {
    setMessage(''); setBusy(true)
    try { const result = await api(`/api/subjects/${encodeURIComponent(name)}`); setSelected(result.data); setTopicIndex(0); setTab('topics') }
    catch (err) { setMessage(err.message) } finally { setBusy(false) }
  }
  function logout() {
    sessionStorage.removeItem('user')
    sessionStorage.removeItem('token')
    setUser(null); setSelected(null); setSubjects([]); setSummary(null); setView('subjects')
  }

  if (!user) return <main className="auth-shell"><section className="auth-card"><div className="brand-mark"><GraduationCap size={30}/></div><p className="eyebrow">YOUR LEARNING WORKSPACE</p><h1>Centralized <span>E-Study</span></h1><p className="muted intro">Learn core computer science concepts. Prepare smarter for technical interviews.</p><div className="switch"><button className={mode==='login'?'active':''} onClick={()=>{setMode('login');setMessage('')}}>Login</button><button className={mode==='register'?'active':''} onClick={()=>{setMode('register');setMessage('')}}>Create account</button></div><form onSubmit={submit}><label>Username</label><input value={username} onChange={e=>setUsername(e.target.value)} placeholder="Enter your username" autoComplete="username" required/><label>Password</label><input type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="At least 8 characters" autoComplete={mode==='login'?'current-password':'new-password'} required minLength={mode==='register'?8:1}/><button className="primary full" disabled={busy}>{busy?'Please wait…':mode==='login'?'Sign in':'Create account'}</button></form>{message&&<p className="notice">{message}</p>}<p className="footnote"><ShieldCheck size={14}/> Secure password hashing enabled</p></section></main>

  return <div className="app-shell"><aside className="sidebar"><div className="side-brand"><div className="brand-mark small"><GraduationCap size={22}/></div><div><b>E-Study</b><small>LEARNING PLATFORM</small></div></div><div className="side-label">WORKSPACE</div><button className={view==='subjects'?'nav-item selected':'nav-item'} onClick={()=>{setSelected(null);setView('subjects')}}><BookOpen size={17}/> Subjects</button><button className={view==='profile'?'nav-item selected':'nav-item'} onClick={()=>{setSelected(null);setView('profile')}}><CircleUserRound size={17}/> Profile &amp; progress</button><div className="side-note"><Brain size={18}/><b>Keep learning</b><p>Your study material and interview preparation topics are in one place.</p></div><div className="side-user"><CircleUserRound size={19}/><div><b>{user.username}</b><small>{user.role || 'STUDENT'} account</small></div><button title="Logout" onClick={logout}><LogOut size={17}/></button></div></aside><main className="main-area"><header className="topbar"><div><span className="muted">LEARNING / </span><b>{view==='profile'?'MY PROGRESS':(selected?.subject || 'DASHBOARD')}</b></div><div className="top-right"><span className="status-dot"/> Platform online</div></header>{view==='profile'?<Profile user={user} summary={summary}/>:selected?<><button className="back-link" onClick={()=>setSelected(null)}><ChevronLeft size={17}/> All subjects</button><section className="page-heading"><p className="eyebrow">SUBJECT LIBRARY</p><h1>{selected.subject}</h1><p className="muted">{selected.topics?.length||0} topics · {selected.questions?.length||0} interview questions</p></section><div className="tabs"><button className={tab==='topics'?'active':''} onClick={()=>setTab('topics')}> <Code2 size={16}/> Topics</button><button className={tab==='qna'?'active':''} onClick={()=>setTab('qna')}>Interview Q&amp;A</button></div>{tab==='topics'?<div className="study-layout"><nav className="topic-list">{(selected.topics||[]).map((t,i)=><button key={i} className={topicIndex===i?'active':''} onClick={()=>setTopicIndex(i)}><span>{String(i+1).padStart(2,'0')}</span>{t.topicName}{isDone(selected.subject,t.topicName)&&<Check size={13} className="tick"/>}</button>)}</nav><article className="content-card">{selected.topics?.[topicIndex]?<><p className="eyebrow">TOPIC {topicIndex+1} OF {selected.topics.length}</p><h2>{selected.topics[topicIndex].topicName}</h2><h3>Concept</h3><p className="theory">{selected.topics[topicIndex].theory}</p>{selected.topics[topicIndex].example&&<><h3>Example</h3><blockquote>{selected.topics[topicIndex].example}</blockquote></>}{selected.topics[topicIndex].code&&<><h3>Code example</h3><pre><code>{selected.topics[topicIndex].code}</code></pre></>}<div className="study-actions"><button className="outline" onClick={markStudied}><Check size={15}/> {isDone(selected.subject,selected.topics[topicIndex].topicName)?'Studied':'Mark as studied'}</button><button className="primary" onClick={()=>setQuizTopic({subject:selected.subject,topicName:selected.topics[topicIndex].topicName})}><Sparkles size={15}/> Take AI quiz</button></div><div className="content-actions"><button className="outline" disabled={topicIndex===0} onClick={()=>setTopicIndex(i=>i-1)}>Previous</button><button className="primary" disabled={topicIndex===selected.topics.length-1} onClick={()=>setTopicIndex(i=>i+1)}>Next topic <PlayCircle size={16}/></button></div></>:<p>No topics available.</p>}</article></div>:<div className="qna-grid">{(selected.questions||[]).map((q,i)=><details key={i} className="qna"><summary>Q{i+1}. {q.question}</summary><p>{q.answer}</p></details>)}</div>}</>:<><section className="welcome"><div><p className="eyebrow">YOUR LEARNING JOURNEY</p><h1>Welcome back, {user.username}</h1><p className="muted">Choose a subject and continue building your technical foundation.</p></div><div className="welcome-icon"><Brain size={42}/></div></section><StatsStrip summary={summary}/><div className="section-title"><div><h2>Explore subjects</h2><p className="muted">Select a subject to view topics and interview questions.</p></div><div className="search"><Search size={16}/><input value={query} onChange={e=>setQuery(e.target.value)} placeholder="Search subjects"/></div></div>{message&&<p className="notice">{message}</p>}<div className="subject-grid">{subjects.filter(s=>s.toLowerCase().includes(query.toLowerCase())).map((s,i)=><button className="subject-card" key={s} onClick={()=>openSubject(s)}><div className="subject-icon">{['☕','🧩','💻','🌐','🗄️'][i%5]}</div><div className="card-top"><span>SUBJECT {String(i+1).padStart(2,'0')}</span><BookOpen size={17}/></div><h3>{s}</h3><p>Explore concepts, practical examples and interview preparation questions.</p><ProgressBar sp={progressOf(s)}/><span className="card-link">Open subject <PlayCircle size={15}/></span></button>)}</div>{subjects.length===0&&<p className="muted">Loading subjects or no subjects found.</p>}</>}</main>{quizTopic&&<Quiz subject={quizTopic.subject} topicName={quizTopic.topicName} onClose={()=>setQuizTopic(null)}/>}</div>
}

createRoot(document.getElementById('root')).render(<App />)
