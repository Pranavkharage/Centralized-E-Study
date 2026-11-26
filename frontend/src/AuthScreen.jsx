import React, { useState } from 'react'
import { GraduationCap, Sun, Moon, ShieldCheck, Sparkles, BookOpen, TrendingUp } from 'lucide-react'
import { api } from './api.js'
import { BRANCHES, YEARS } from './constants.js'

const empty = { firstName: '', lastName: '', email: '', phone: '', branch: '', studyYear: '', username: '', password: '', confirm: '' }

export default function AuthScreen({ onLoggedIn, theme, toggleTheme }) {
  const [mode, setMode] = useState('login')
  const [login, setLogin] = useState({ username: '', password: '' })
  const [form, setForm] = useState(empty)
  const [message, setMessage] = useState({ text: '', ok: false })
  const [busy, setBusy] = useState(false)
  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }))

  async function submitLogin(e) {
    e.preventDefault(); setMessage({ text: '' }); setBusy(true)
    try {
      const r = await api('/api/auth/login', { method: 'POST', body: JSON.stringify(login) })
      const { accessToken, ...user } = r.data
      sessionStorage.setItem('token', accessToken)
      sessionStorage.setItem('user', JSON.stringify(user))
      onLoggedIn(user)
    } catch (err) { setMessage({ text: err.message, ok: false }) } finally { setBusy(false) }
  }

  async function submitRegister(e) {
    e.preventDefault(); setMessage({ text: '' })
    if (form.password !== form.confirm) { setMessage({ text: 'Passwords do not match', ok: false }); return }
    setBusy(true)
    try {
      const { confirm, ...body } = form
      await api('/api/auth/register', { method: 'POST', body: JSON.stringify(body) })
      setLogin({ username: form.username, password: '' })
      setForm(empty); setMode('login')
      setMessage({ text: 'Account created. Please log in.', ok: true })
    } catch (err) { setMessage({ text: err.message, ok: false }) } finally { setBusy(false) }
  }

  return (
    <main className="auth-wrap">
      <section className="auth-hero">
        <div className="brand"><div className="brand-mark"><GraduationCap size={26} /></div><b>E-Study</b></div>
        <h1>Study smarter.<br />Get interview ready.</h1>
        <p>Core computer science concepts, explained simply and tested with quizzes built from the same material.</p>
        <ul className="hero-points">
          <li><BookOpen size={18} /> Structured notes with examples and code</li>
          <li><Sparkles size={18} /> AI quizzes generated from each topic</li>
          <li><TrendingUp size={18} /> Track your progress and quiz scores</li>
        </ul>
      </section>

      <section className="auth-panel">
        <button className="icon-circle auth-theme" onClick={toggleTheme} title="Toggle light / dark mode">
          {theme === 'dark' ? <Sun size={18} /> : <Moon size={18} />}
        </button>
        <div className="auth-box">
          <h2>{mode === 'login' ? 'Welcome back' : 'Create your account'}</h2>
          <p className="muted">{mode === 'login' ? 'Sign in with your username and password.' : 'Tell us a little about you to get started.'}</p>
          <div className="pill-tabs">
            <button className={mode === 'login' ? 'active' : ''} onClick={() => { setMode('login'); setMessage({ text: '' }) }}>Login</button>
            <button className={mode === 'register' ? 'active' : ''} onClick={() => { setMode('register'); setMessage({ text: '' }) }}>Create account</button>
          </div>

          {mode === 'login' ? (
            <form onSubmit={submitLogin} className="form-grid one">
              <label className="field"><span>Username</span>
                <input value={login.username} onChange={e => setLogin({ ...login, username: e.target.value })} autoComplete="username" required /></label>
              <label className="field"><span>Password</span>
                <input type="password" value={login.password} onChange={e => setLogin({ ...login, password: e.target.value })} autoComplete="current-password" required /></label>
              <button className="primary full" disabled={busy}>{busy ? 'Please wait…' : 'Sign in'}</button>
            </form>
          ) : (
            <form onSubmit={submitRegister} className="form-grid">
              <label className="field"><span>First name</span><input value={form.firstName} onChange={set('firstName')} required /></label>
              <label className="field"><span>Last name (surname)</span><input value={form.lastName} onChange={set('lastName')} required /></label>
              <label className="field"><span>Email</span><input type="email" value={form.email} onChange={set('email')} required /></label>
              <label className="field"><span>Phone number</span><input type="tel" value={form.phone} onChange={set('phone')} placeholder="10 digit mobile" required /></label>
              <label className="field"><span>Branch</span>
                <select value={form.branch} onChange={set('branch')} required><option value="">Select branch</option>{BRANCHES.map(b => <option key={b}>{b}</option>)}</select></label>
              <label className="field"><span>Year of study</span>
                <select value={form.studyYear} onChange={set('studyYear')} required><option value="">Select year</option>{YEARS.map(y => <option key={y}>{y}</option>)}</select></label>
              <label className="field full"><span>Username</span><input value={form.username} onChange={set('username')} minLength={3} autoComplete="username" required /></label>
              <label className="field"><span>Password</span><input type="password" value={form.password} onChange={set('password')} minLength={8} placeholder="At least 8 characters" autoComplete="new-password" required /></label>
              <label className="field"><span>Confirm password</span><input type="password" value={form.confirm} onChange={set('confirm')} autoComplete="new-password" required /></label>
              <button className="primary full" disabled={busy}>{busy ? 'Please wait…' : 'Create account'}</button>
            </form>
          )}

          {message.text && <p className={'notice' + (message.ok ? ' ok' : '')}>{message.text}</p>}
          <p className="footnote"><ShieldCheck size={14} /> Passwords are stored securely using hashing.</p>
        </div>
      </section>
    </main>
  )
}
