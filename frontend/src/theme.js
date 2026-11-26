import { useEffect, useState } from 'react'

const read = () => {
  try {
    const saved = localStorage.getItem('theme')
    if (saved) return saved
  } catch { /* storage blocked: fall through */ }
  return window.matchMedia?.('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

// Light/dark theme, remembered in the browser
export function useTheme() {
  const [theme, setTheme] = useState(read)
  useEffect(() => {
    document.documentElement.dataset.theme = theme
    try { localStorage.setItem('theme', theme) } catch { /* ignore */ }
  }, [theme])
  return [theme, () => setTheme(t => (t === 'dark' ? 'light' : 'dark'))]
}
