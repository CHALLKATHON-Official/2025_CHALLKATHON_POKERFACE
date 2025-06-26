import { apiClient } from './config'
import type { ApiResponse } from './config'

// 사용자 타입 정의
export interface User {
  id: number
  username: string
  email: string
  profileImageUrl?: string
  provider: string
}

// 로그인 요청 타입
export interface LoginRequest {
  username: string
  password: string
}

// 회원가입 요청 타입
export interface SignupRequest {
  username: string
  email: string
  password: string
}

// 비밀번호 변경 요청 타입
export interface PasswordChangeRequest {
  currentPassword: string
  newPassword: string
}

// 이메일 변경 요청 타입
export interface EmailChangeRequest {
  newEmail: string
}

// 프로필 업데이트 요청 타입
export interface ProfileUpdateRequest {
  newUsername: string
  profileImageUrl: string
}

// 로그인 응답 타입
export interface LoginResponse {
  accessToken: string
  refreshToken: string
  user: User
}

// 인증 API 함수들
export const authAPI = {
  // 로그인
  login: async (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    const response = await apiClient.post('/api/auth/login', data)
    return response.data
  },

  // 회원가입
  signup: async (data: SignupRequest): Promise<ApiResponse<User>> => {
    const response = await apiClient.post('/api/auth/signup', data)
    return response.data
  },

  // 현재 사용자 정보 조회
  getCurrentUser: async (): Promise<ApiResponse<User>> => {
    const response = await apiClient.get('/api/auth/users/me')
    return response.data
  },

  // 비밀번호 변경
  changePassword: async (data: PasswordChangeRequest): Promise<ApiResponse<void>> => {
    const response = await apiClient.patch('/api/auth/users/password', data)
    return response.data
  },

  // 이메일 변경
  changeEmail: async (data: EmailChangeRequest): Promise<ApiResponse<void>> => {
    const response = await apiClient.patch('/api/auth/users/email', data)
    return response.data
  },

  // 프로필 업데이트
  updateProfile: async (data: ProfileUpdateRequest): Promise<ApiResponse<void>> => {
    const response = await apiClient.patch('/api/auth/users/profile', data)
    return response.data
  },

  // 회원 탈퇴
  deleteUser: async (): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete('/api/auth/users')
    return response.data
  },

  // 토큰 갱신
  refreshToken: async (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> => {
    const response = await apiClient.post('/api/auth/refresh', { refreshToken })
    return response.data
  },
} 