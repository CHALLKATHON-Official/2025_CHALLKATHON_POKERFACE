import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import './Login.css'

export default function Login() {
  const navigate = useNavigate()
  const location = useLocation()
  const { login } = useAuth()
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  })
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
    
    if (!formData.username || !formData.password) {
      setError('아이디와 비밀번호를 모두 입력해주세요.')
      return
    }

    setIsLoading(true)
    setError('')

    try {
      const success = await login(formData.username, formData.password)
      
      if (success) {
        // 메인 페이지로 이동
        navigate('/')
      } else {
        setError('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.')
      }
    } catch (err: any) {
      console.error('Login error:', err)
      setError('로그인 중 오류가 발생했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1>Memorized</h1>
          <p>시간을 수집하는 공간</p>
        </div>
        
        {location.state?.message && (
          <div className="login-message">
            {location.state.message}
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <input
              type="text"
              name="username"
              placeholder="아이디"
              value={formData.username}
              onChange={handleChange}
              disabled={isLoading}
              required
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
            />
          </div>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}
          
          <button 
            type="submit" 
            className="login-btn"
            disabled={isLoading}
          >
            {isLoading ? '로그인 중...' : '로그인'}
          </button>
        </form>
        
        <div className="login-footer">
          <p>계정이 없으신가요? <span onClick={() => navigate('/signup')}>회원가입</span></p>
        </div>
      </div>
    </div>
  )
} 