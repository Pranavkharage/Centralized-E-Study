import React, { useEffect, useState } from 'react'
import { BookOpen, TrendingUp, ShieldCheck, LogOut, Sun, Moon, GraduationCap, Brain, Search, PlayCircle } from 'lucide-react'
import { api } from './api.js'
import { useTheme } from './theme.js'
import { displayName } from './constants.js'
import AuthScreen from './AuthScreen.jsx'
import SubjectView from './SubjectView.jsx'
import Admin from './Admin.jsx'
import Quiz from './Quiz.jsx'
import { Avatar, ProgressBar, StatsStrip, ProgressPage, AccountPage } from './Components.jsx'

const readUser = () => { try { return JSON.parse(sessionStorage.getItem('user')) } catch { return null } }
const ICONS = ['☕', '🧩', '💻', '🌐', '🗄️']

export default function App() {
  const [theme, toggleTheme] = useTheme()
  const [user, setUser] = useState(readUser)
  const [view, setView] = useState('subjects')       // subjects | progress | account | admin
  const [subjects, setSubjects] = useState([])
  const [selected, setSelected] = useState(null)
  const [summary, setSummary] = useState(null)
  const [quizTopic, setQuizTopic] = useState(null)
  const [query, setQuery] = useState('')
  const [message, setMessage] = useState('')

  const saveUser = u => { setUser(u); sessionStorage.setItem('user', JSON.stringify(u)) }
  const loadSummary = () => api('/api/progress/summary').then(r => setSummary(r.data)).catch(() => {})
  const progressOf = name => summary?.subjects?.find(x => x.subject === name)
  const isDone = (sub, topic) => !!progressOf(sub)?.completed?.includes(topic)
  const isAdmin = user?.role === 'ADMIN'

  // refresh profile details (older sessions do not have name/email yet)
  useEffect(() => { if (user) api('/api/users/me').then(r => saveUser(r.data)).catch(() => {}) }, [])
  useEffect(() => { if (user) loadSummary() }, [user?.username, view, selected, quizTopic])
  useEffect(() => {
    if (user && view === 'subjects' && !selected) api('/api/subjects/all').then(r => setSubjects(r.data || [])).catch(e => setMessage(e.message))
  }, [user?.username, view, selected])

  async function openSubject(name) {
    setMessage('')
    try { setSelected((await api(`/api/subjects/${encodeURIComponent(name)}`)).data) } catch (e) { setMessage(e.message) }
  }
  function go(v) { setSelected(null); setView(v) }
  function logout() {
    sessionStorage.clear()
    setUser(null); setSelected(null); setSubjects([]); setSummary(null); setView('subjects')
  }

  if (!user) return <AuthScreen onLoggedIn={u => { setUser(u); setView('subjects') }} theme={theme} toggleTheme={toggleTheme} />

  const crumb = view === 'progress' ? 'My progress' : view === 'account' ? 'My profile' : view === 'admin' ? 'Admin panel' : selected ? selected.subject : 'Dashboard'

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="side-brand"><div className="brand-mark small"><GraduationCap size={20} /></div><div><b>E-Study</b><small>Learning platform</small></div></div>
        <div className="side-label">Workspace</div>
        <button className={'nav-item' + (view === 'subjects' ? ' selected' : '')} onClick={() => go('subjects')}><BookOpen size={17} /> Subjects</button>
        <button className={'nav-item' + (view === 'progress' ? ' selected' : '')} onClick={() => go('progress')}><TrendingUp size={17} /> My progress</button>
        {isAdmin && <>
          <div className="side-label">Admin</div>
          <button className={'nav-item' + (view === 'admin' ? ' selected' : '')} onClick={() => go('admin')}><ShieldCheck size={17} /> Admin panel</button>
        </>}
        <div className="side-spacer" />
        <div className="side-user">
          <button className="side-user-main" onClick={() => go('account')} title="My profile">
            <Avatar user={user} size={34} />
            <div><b>{displayName(user)}</b><small>{isAdmin ? 'Administrator' : 'Student'}</small></div>
          </button>
          <button className="icon-btn" title="Log out" onClick={logout}><LogOut size={17} /></button>
        </div>
      </aside>

      <div className="main">
        <header className="topbar">
          <div className="crumb"><span className="muted">Learning /</span> <b>{crumb}</b></div>
          <div className="top-right">
            <button className="icon-circle" onClick={toggleTheme} title="Toggle light / dark mode">{theme === 'dark' ? <Sun size={18} /> : <Moon size={18} />}</button>
            <button className={'avatar-btn' + (view === 'account' ? ' on' : '')} onClick={() => go('account')} title="My profile"><Avatar user={user} size={38} /></button>
          </div>
        </header>

        <div className="content">
          {view === 'progress' && <ProgressPage user={user} summary={summary} />}
          {view === 'account' && <AccountPage user={user} onSaved={saveUser} />}
          {view === 'admin' && isAdmin && <Admin me={user} />}
          {view === 'subjects' && (selected
            ? <SubjectView key={selected.subject} subject={selected} isDone={isDone} onBack={() => setSelected(null)} onQuiz={setQuizTopic} onProgress={loadSummary} />
            : <>
                <section className="welcome">
                  <div>
                    <p className="eyebrow">YOUR LEARNING JOURNEY</p>
                    <h1>Welcome back, {displayName(user)}</h1>
                    <p className="muted">Choose a subject and continue building your technical foundation.</p>
                  </div>
                  <div className="welcome-icon"><Brain size={40} /></div>
                </section>
                {!user.firstName && <div className="banner">Add your name and details to complete your profile. <button className="outline small" onClick={() => go('account')}>Complete profile</button></div>}
                <StatsStrip summary={summary} />
                <div className="section-title">
                  <div><h2>Explore subjects</h2><p className="muted">Select a subject to view topics and interview questions.</p></div>
                  <div className="search"><Search size={16} /><input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search subjects" /></div>
                </div>
                {message && <p className="notice">{message}</p>}
                <div className="subject-grid">
                  {subjects.filter(s => s.toLowerCase().includes(query.toLowerCase())).map((s, n) => (
                    <button className="subject-card" key={s} onClick={() => openSubject(s)}>
                      <div className="subject-icon">{ICONS[n % ICONS.length]}</div>
                      <h3>{s}</h3>
                      <p>Concepts, practical examples and interview questions.</p>
                      <ProgressBar sp={progressOf(s)} />
                      <span className="card-link">Open subject <PlayCircle size={15} /></span>
                    </button>
                  ))}
                </div>
                {subjects.length === 0 && !message && <p className="muted">Loading subjects…</p>}
              </>)}
        </div>
      </div>
      {quizTopic && <Quiz subject={quizTopic.subject} topicName={quizTopic.topicName} onClose={() => setQuizTopic(null)} />}
    </div>
  )
}
