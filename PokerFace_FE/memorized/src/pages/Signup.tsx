import { useState } from 'react'
import { Link } from 'react-router-dom'
import './Signup.css'

export default function Signup() {
  const [id, setId] = useState('')
  const [pw, setPw] = useState('')
  const [pw2, setPw2] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!id || !pw || !pw2) {
      setError('모든 항목을 입력하세요.')
      return
    }
    if (pw !== pw2) {
      setError('비밀번호가 일치하지 않습니다.')
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
          <input type="password" placeholder="비밀번호 확인" className="auth-input" value={pw2} onChange={e => setPw2(e.target.value)} />
          {error && <div className="form-error">{error}</div>}
          <button type="submit" className="auth-btn">가입하기</button>
        </form>
        <div className="auth-divider" />
        <div className="auth-bottom-text">
          계정이 있으신가요? <Link to="/login" className="auth-link">로그인</Link>
        </div>
      </div>
      <footer className="auth-footer">© 2025 Memorized</footer>
    </div>
  )
} 