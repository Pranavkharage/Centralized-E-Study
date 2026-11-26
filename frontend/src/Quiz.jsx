import React, { useEffect, useState } from 'react'
import { X } from 'lucide-react'
import { api } from './api.js'

export default function Quiz({ subject, topicName, onClose }) {
  const [state, setState] = useState('loading') // loading | taking | result | error
  const [quiz, setQuiz] = useState(null)
  const [answers, setAnswers] = useState([])
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function start() {
    setState('loading'); setError(''); setResult(null)
    try {
      const r = await api('/api/quiz/start', { method: 'POST', body: JSON.stringify({ subject, topicName, count: 5 }) })
      setQuiz(r.data)
      setAnswers(Array(r.data.questions.length).fill(null))
      setState('taking')
    } catch (e) { setError(e.message); setState('error') }
  }
  useEffect(() => { start() }, [])

  async function submit() {
    setBusy(true); setError('')
    try {
      const r = await api('/api/quiz/submit', { method: 'POST', body: JSON.stringify({ attemptId: quiz.attemptId, answers }) })
      setResult(r.data); setState('result')
    } catch (e) { setError(e.message) } finally { setBusy(false) }
  }

  const pct = result && result.total ? Math.round((100 * result.score) / result.total) : 0

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <div className="modal-head">
          <div><p className="eyebrow">AI QUIZ · {subject}</p><h2>{topicName}</h2></div>
          <button className="icon-btn" onClick={onClose} title="Close"><X size={18} /></button>
        </div>

        {state === 'loading' && <p className="muted pad">Preparing your quiz… the first quiz for a topic can take 10–20 seconds.</p>}
        {state === 'error' && <><p className="notice">{error}</p><div className="modal-actions"><button className="outline" onClick={onClose}>Close</button><button className="primary" onClick={start}>Try again</button></div></>}

        {state === 'taking' && <>
          {quiz.questions.map((q, qi) => (
            <div className="quiz-q" key={q.index}>
              <p><b>Q{qi + 1}.</b> {q.question}</p>
              {q.options.map((o, oi) => (
                <label key={oi} className={'opt' + (answers[qi] === oi ? ' selected' : '')}>
                  <input type="radio" name={'q' + qi} checked={answers[qi] === oi} onChange={() => setAnswers(a => a.map((v, i) => (i === qi ? oi : v)))} />
                  {o}
                </label>
              ))}
            </div>
          ))}
          {error && <p className="notice">{error}</p>}
          <div className="modal-actions">
            <span className="muted">{answers.filter(a => a !== null).length}/{answers.length} answered</span>
            <button className="primary" disabled={busy || answers.some(a => a === null)} onClick={submit}>{busy ? 'Checking…' : 'Submit quiz'}</button>
          </div>
        </>}

        {state === 'result' && <>
          <div className={'result-banner ' + (pct >= 70 ? 'good' : pct >= 40 ? 'mid' : 'low')}>
            <b>{result.score}/{result.total}</b><span>{pct}% correct</span>
          </div>
          {result.review.map((r, i) => (
            <div className="quiz-q" key={i}>
              <p><b>Q{i + 1}.</b> {r.question}</p>
              {r.options.map((o, oi) => (
                <div key={oi} className={'opt review' + (oi === r.correctIndex ? ' correct' : oi === r.yourAnswer ? ' wrong' : '')}>
                  {o}{oi === r.correctIndex ? '  ✓' : oi === r.yourAnswer ? '  ✗ your answer' : ''}
                </div>
              ))}
              {r.explanation && <p className="explain">{r.explanation}</p>}
            </div>
          ))}
          <div className="modal-actions">
            <button className="outline" onClick={onClose}>Close</button>
            <button className="primary" onClick={start}>Try another quiz</button>
          </div>
        </>}
      </div>
    </div>
  )
}
