export const BRANCHES = ['Computer Engineering', 'Information Technology', 'Computer Science', 'AI & Data Science',
  'Electronics & Telecommunication', 'Electrical Engineering', 'Mechanical Engineering', 'Civil Engineering', 'Other']
export const YEARS = ['First Year', 'Second Year', 'Third Year', 'Final Year', 'Graduated']

export const displayName = u => [u?.firstName, u?.lastName].filter(Boolean).join(' ') || u?.username || ''
export const initials = u => (((u?.firstName || '')[0] || '') + ((u?.lastName || '')[0] || '') || (u?.username || '?')[0]).toUpperCase()
