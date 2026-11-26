import React, { useState } from 'react'
import { ChevronLeft, Code2, Check, Sparkles, PlayCircle } from 'lucide-react'
import { api } from './api.js'

export default function SubjectView({ subject, isDone, onBack, onQuiz, onProgress }) {
  const [tab, setTab] = useState('topics')
  const [i, setI] = useState(0)
  const topics = subject.topics || []
  const t = topics[i]

  async function markStudied() {
    try {
      await api('/api/progress/topic', { method: 'POST', body: JSON.stringify({ subject: subject.subject, topicName: t.topicName }) })
      onProgress()
    } catch (e) { alert(e.message) }
  }

  return (
    <>
      <button className="back-link" onClick={onBack}><ChevronLeft size={17} /> All subjects</button>
      <section className="page-heading">
        <p className="eyebrow">SUBJECT LIBRARY</p>
        <h1>{subject.subject}</h1>
        <p className="muted">{topics.length} topics · {subject.questions?.length || 0} interview questions</p>
      </section>
      <div className="tabs">
        <button className={tab === 'topics' ? 'active' : ''} onClick={() => setTab('topics')}><Code2 size={16} /> Topics</button>
        <button className={tab === 'qna' ? 'active' : ''} onClick={() => setTab('qna')}>Interview Q&amp;A</button>
      </div>

      {tab === 'topics' ? (
        <div className="study-layout">
          <nav className="topic-list">
            {topics.map((x, n) => (
              <button key={n} className={i === n ? 'active' : ''} onClick={() => setI(n)}>
                <span>{String(n + 1).padStart(2, '0')}</span>{x.topicName}
                {isDone(subject.subject, x.topicName) && <Check size={14} className="tick" />}
              </button>
            ))}
          </nav>
          <article className="content-card">
            {t ? <>
              <p className="eyebrow">TOPIC {i + 1} OF {topics.length}</p>
              <h2>{t.topicName}</h2>
              <h3>Concept</h3><p className="theory">{t.theory}</p>
              {t.example && <><h3>Example</h3><blockquote>{t.example}</blockquote></>}
              {t.code && <><h3>Code example</h3><pre><code>{t.code}</code></pre></>}
              <div className="study-actions">
                <button className="outline" onClick={markStudied}><Check size={15} /> {isDone(subject.subject, t.topicName) ? 'Studied' : 'Mark as studied'}</button>
                <button className="primary" onClick={() => onQuiz({ subject: subject.subject, topicName: t.topicName })}><Sparkles size={15} /> Take AI quiz</button>
              </div>
              <div className="content-actions">
                <button className="outline" disabled={i === 0} onClick={() => setI(n => n - 1)}>Previous</button>
                <button className="primary" disabled={i === topics.length - 1} onClick={() => setI(n => n + 1)}>Next topic <PlayCircle size={16} /></button>
              </div>
            </> : <p className="muted">No topics available yet.</p>}
          </article>
        </div>
      ) : (
        <div className="qna-grid">
          {(subject.questions || []).map((q, n) => <details key={n} className="qna"><summary>Q{n + 1}. {q.question}</summary><p>{q.answer}</p></details>)}
          {!(subject.questions || []).length && <p className="muted">No interview questions yet.</p>}
        </div>
      )}
    </>
  )
}
