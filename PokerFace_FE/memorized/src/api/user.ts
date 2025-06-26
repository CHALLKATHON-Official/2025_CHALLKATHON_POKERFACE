import { apiClient } from './config'
import type { ApiResponse } from './config'

// 사용자 검색 응답 타입
export interface UserSearchResponse {
  id: number
  username: string
  email: string
  profileImageUrl?: string
}

// 사용자 검색 히스토리 응답 타입
export interface UserSearchHistoryResponse {
  id: number
  query: string
  searchedAt: string
}

// 사용자 검색 제안 응답 타입
export interface UserSearchSuggestResponse {
  suggestions: string[]
}

// 사용자 API 함수들
export const userAPI = {
  // 사용자 검색
  searchUsers: async (query: string): Promise<ApiResponse<UserSearchResponse[]>> => {
    const response = await apiClient.get('/api/users/search', {
      params: { query }
    })
    return response.data
  },

  // 검색 히스토리 조회
  getSearchHistory: async (): Promise<ApiResponse<UserSearchHistoryResponse[]>> => {
    const response = await apiClient.get('/api/users/search/history')
    return response.data
  },

  // 검색 히스토리 삭제
  deleteSearchHistory: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete(`/api/users/search/history/${id}`)
    return response.data
  },

  // 검색 키워드 제안
  suggestKeywords: async (prefix?: string): Promise<ApiResponse<UserSearchSuggestResponse>> => {
    const response = await apiClient.get('/api/users/search/suggest', {
      params: { prefix }
    })
    return response.data
  }
} 