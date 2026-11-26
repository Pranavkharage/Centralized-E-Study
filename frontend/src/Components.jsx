import React from 'react'
import { CircleUserRound } from 'lucide-react'

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

export function Profile({ user, summary }) {
  return (
    <>
      <section className="page-heading">
        <p className="eyebrow">MY PROFILE</p>
        <h1>Study progress &amp; quiz results</h1>
      </section>
      <div className="profile-card">
        <div className="avatar"><CircleUserRound size={34} /></div>
        <div>
          <b>{user.username}</b>
          <span className="muted">{user.role || 'STUDENT'} account</span>
        </div>
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
        : <table className="results">
            <thead><tr><th>Subject</th><th>Topic</th><th>Score</th><th>Date</th></tr></thead>
            <tbody>
              {summary.recentAttempts.map(a => {
                const pct = a.total ? Math.round((100 * a.score) / a.total) : 0
                return (
                  <tr key={a.id}>
                    <td>{a.subject}</td>
                    <td>{a.topicName}</td>
                    <td><span className={'score ' + (pct >= 70 ? 'good' : pct >= 40 ? 'mid' : 'low')}>{a.score}/{a.total} · {pct}%</span></td>
                    <td>{a.submittedAt ? new Date(a.submittedAt).toLocaleDateString() : ''}</td>
                  </tr>
                )
              })}
            </tbody>
          </table>}
    </>
  )
}
