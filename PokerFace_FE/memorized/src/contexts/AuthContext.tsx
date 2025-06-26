import { createContext, useContext, useState, useEffect } from 'react'
import type { ReactNode } from 'react'
import { authAPI } from '../api/auth'
import type { User } from '../api/auth'

interface AuthContextType {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (username: string, password: string) => Promise<boolean>
  logout: () => void
  signup: (username: string, email: string, password: string) => Promise<boolean>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

interface AuthProviderProps {
  children: ReactNode
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  // 초기 사용자 정보 로드
  useEffect(() => {
    const initializeAuth = async () => {
      const token = localStorage.getItem('accessToken')
      const refreshToken = localStorage.getItem('refreshToken')
      if (token) {
        try {
          const response = await authAPI.getCurrentUser()
          if (response.success) {
            setUser(response.data)
          } else if (refreshToken) {
            // accessToken 만료 시 refreshToken으로 갱신 시도
            const refreshRes = await authAPI.refreshToken(refreshToken)
            if (refreshRes.success) {
              localStorage.setItem('accessToken', refreshRes.data.accessToken)
              // 갱신된 토큰으로 다시 사용자 정보 요청
              const userRes = await authAPI.getCurrentUser()
              if (userRes.success) {
                setUser(userRes.data)
                setIsLoading(false)
                return
              }
            }
            // refreshToken도 만료/실패 시 로그아웃 처리
            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
            localStorage.removeItem('user')
          } else {
            // 토큰이 유효하지 않으면 제거
            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
            localStorage.removeItem('user')
          }
        } catch (error) {
          localStorage.removeItem('accessToken')
          localStorage.removeItem('refreshToken')
          localStorage.removeItem('user')
        }
      }
      setIsLoading(false)
    }

    initializeAuth()
  }, [])

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      const response = await authAPI.login({ username, password })
      if (response.success) {
        // 백엔드에서 토큰을 반환하지 않을 수 있으므로, 
        // 사용자 정보만 저장하고 토큰은 별도로 관리
        const userData = response.data.user || response.data
        setUser(userData)
        localStorage.setItem('user', JSON.stringify(userData))
        
        // 토큰이 있다면 저장
        if (response.data.accessToken) {
          localStorage.setItem('accessToken', response.data.accessToken)
        }
        if (response.data.refreshToken) {
          localStorage.setItem('refreshToken', response.data.refreshToken)
        }
        
        return true
      }
      return false
    } catch (error) {
      console.error('Login error:', error)
      return false
    }
  }

  const logout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    setUser(null)
  }

  const signup = async (username: string, email: string, password: string): Promise<boolean> => {
    try {
      const response = await authAPI.signup({ username, email, password })
      return response.success
    } catch (error) {
      console.error('Signup error:', error)
      return false
    }
  }

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
    signup,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
} 