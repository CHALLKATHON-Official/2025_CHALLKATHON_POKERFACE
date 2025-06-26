import axios from 'axios'

// API 응답 타입
export interface ApiResponse<T = any> {
  success: boolean
  data: T
  message?: string
}

// API 클라이언트 설정
export const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // 백엔드 서버 주소
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청 인터셉터 - 토큰 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터 - 토큰 갱신 및 에러 처리
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config

    // 401 에러이고 토큰 갱신을 시도하지 않았다면
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      const refreshToken = localStorage.getItem('refreshToken')
      if (refreshToken) {
        try {
          // 토큰 갱신 시도
          const response = await axios.post('http://localhost:8080/api/auth/refresh', {
            refreshToken
          })
          
          if (response.data.success) {
            localStorage.setItem('accessToken', response.data.data.accessToken)
            originalRequest.headers.Authorization = `Bearer ${response.data.data.accessToken}`
            return apiClient(originalRequest)
          }
        } catch (refreshError) {
          // 토큰 갱신 실패 시 로그아웃
          localStorage.removeItem('accessToken')
          localStorage.removeItem('refreshToken')
          localStorage.removeItem('user')
          window.location.href = '/login'
        }
      }
    }

    return Promise.reject(error)
  }
)

// 에러 응답 타입
export interface ApiError {
  success: false
  message: string
  errors?: any
} 