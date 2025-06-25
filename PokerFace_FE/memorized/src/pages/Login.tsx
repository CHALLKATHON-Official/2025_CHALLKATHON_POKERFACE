import { useState } from 'react'
import { Link } from 'react-router-dom'
import './Login.css'

export default function Login() {
  const [id, setId] = useState('')
  const [pw, setPw] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!id || !pw) {
      setError('아이디와 비밀번호를 모두 입력하세요.')
      return
    }
    setError('')
    // TODO: API 연동
  }

  return (
    <div className="auth-bg">
      <div className="auth-card">
        <div className="auth-logo">Memorized</div>
        <form className="auth-form" onSubmit={handleSubmit}>
          <input type="text" placeholder="아이디" className="auth-input" value={id} onChange={e => setId(e.target.value)} />
          <input type="password" placeholder="비밀번호" className="auth-input" value={pw} onChange={e => setPw(e.target.value)} />
          {error && <div className="form-error">{error}</div>}
          <button type="submit" className="auth-btn">로그인</button>
        </form>
        <div className="auth-divider" />
        <div className="auth-bottom-text">
          계정이 없으신가요? <Link to="/signup" className="auth-link">가입하기</Link>
        </div>
      </div>
      <footer className="auth-footer">© 2025 Memorized</footer>
    </div>
  )
} 