import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import './Signup.css'

export default function Signup() {
  const navigate = useNavigate()
  const { signup } = useAuth()
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
  })
  const [confirmPassword, setConfirmPassword] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
    // 에러 메시지 초기화
    if (error) setError('')
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.username || !formData.email || !formData.password) {
      setError('모든 필드를 입력해주세요.')
      return
    }

    if (formData.password !== confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.')
      return
    }

    if (formData.password.length < 6) {
      setError('비밀번호는 6자 이상이어야 합니다.')
      return
    }

    setIsLoading(true)
    setError('')

    try {
      const success = await signup(formData.username, formData.email, formData.password)
      
      if (success) {
        // 회원가입 성공 시 로그인 페이지로 이동
        navigate('/login', { 
          state: { message: '회원가입이 완료되었습니다. 로그인해주세요.' }
        })
      } else {
        setError('회원가입에 실패했습니다. 다시 시도해주세요.')
      }
    } catch (err: any) {
      console.error('Signup error:', err)
      setError('회원가입 중 오류가 발생했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="signup-container">
      <div className="signup-card card-glass">
        <div className="signup-header">
          <h1>Memorized</h1>
          <p>새로운 계정 만들기</p>
        </div>
        
        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group">
            <input
              type="text"
              name="username"
              placeholder="아이디"
              value={formData.username}
              onChange={handleChange}
              disabled={isLoading}
              required
              className="input-glass"
            />
          </div>
          
          <div className="form-group">
            <input
              type="email"
              name="email"
              placeholder="이메일"
              value={formData.email}
              onChange={handleChange}
              disabled={isLoading}
              required
              className="input-glass"
            />
          </div>
          
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="비밀번호"
              value={formData.password}
              onChange={handleChange}
              disabled={isLoading}
              required
              className="input-glass"
            />
          </div>

          <div className="form-group">
            <input
              type="password"
              placeholder="비밀번호 확인"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              disabled={isLoading}
              required
              className="input-glass"
            />
          </div>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}
          
          <button 
            type="submit" 
            className="signup-btn btn-main"
            disabled={isLoading}
          >
            {isLoading ? '가입 중...' : '회원가입'}
          </button>
        </form>
        
        <div className="signup-footer">
          <p>이미 계정이 있으신가요? <span onClick={() => navigate('/login')} className="badge-pastel">로그인</span></p>
        </div>
      </div>
    </div>
  )
} 