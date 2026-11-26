// Shared fetch helper: adds the JWT, unwraps {success, message, data}, and logs out on an expired token
export const api = async (path, options = {}) => {
  const token = sessionStorage.getItem('token')
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {})
  }
  const response = await fetch(path, { ...options, headers })
  if (response.status === 401 && token) {
    sessionStorage.clear()
    window.location.reload()
  }
  const data = await response.json().catch(() => ({}))
  if (!response.ok || data.success === false) throw new Error(data.message || `Request failed (${response.status})`)
  return data
}
