import { apiClient } from './config'
import type { ApiResponse } from './config'

// 메모리 타입 정의
export interface Memory {
  id: number
  userId: number
  username: string
  profileImageUrl?: string
  content: string
  emotion?: string
  imageUrl?: string
  createdAt: string
  likeCount: number
  bookmarkCount: number
  isLiked: boolean
  isBookmarked: boolean
}

// 메모리 생성 요청 타입
export interface CreateMemoryRequest {
  content: string
  emotion?: string
  image?: File
}

// 메모리 수정 요청 타입
export interface UpdateMemoryRequest {
  content: string
  emotion?: string
  image?: File
}

// 메모리 목록 응답 타입
export interface MemoryListResponse {
  memories: Memory[]
  totalPages: number
  totalElements: number
  page: number
  size: number
}

// 메모리 상세 응답 타입
export interface MemoryDetailResponse {
  id: number
  content: string
  emotion?: string
  imageUrl?: string
  createdAt: string
  userId: number
  likes: number
  comments: string[]
  username?: string
  profileImageUrl?: string
}

// 메모리 필터 요청 타입
export interface MemoryFilterRequest {
  emotion?: string
  category?: string
  tags?: string[]
  page?: number
  size?: number
}

// 메모리 추천 요청 타입
export interface MemoryRecommendRequest {
  emotion?: string
  type?: string
  size?: number
}

// 메모리 API 함수들
export const memoryAPI = {
  // 메모리 목록 조회 (페이지네이션)
  getMemories: async (type: string = 'all', page: number = 0, size: number = 10): Promise<ApiResponse<MemoryListResponse>> => {
    const response = await apiClient.get('/api/memories', {
      params: { type, page, size }
    })
    return response.data
  },

  // 메모리 상세 조회
  getMemoryDetail: async (id: number): Promise<ApiResponse<MemoryDetailResponse>> => {
    const response = await apiClient.get(`/api/memories/${id}`)
    return response.data
  },

  // 메모리 생성
  createMemory: async (data: CreateMemoryRequest): Promise<ApiResponse<Memory>> => {
    const formData = new FormData()
    formData.append('content', data.content)
    if (data.emotion) {
      formData.append('emotion', data.emotion)
    }
    if (data.image) {
      formData.append('image', data.image)
    }
    
    const response = await apiClient.post('/api/memories', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  // 메모리 수정
  updateMemory: async (memoryId: number, data: UpdateMemoryRequest): Promise<ApiResponse<Memory>> => {
    const formData = new FormData()
    formData.append('content', data.content)
    if (data.emotion) {
      formData.append('emotion', data.emotion)
    }
    if (data.image) {
      formData.append('image', data.image)
    }
    
    const response = await apiClient.put(`/api/memories/${memoryId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  // 메모리 삭제
  deleteMemory: async (memoryId: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete(`/api/memories/${memoryId}`)
    return response.data
  },

  // 좋아요 추가
  addLike: async (memoryId: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.post(`/api/memories/${memoryId}/like`)
    return response.data
  },

  // 좋아요 제거
  removeLike: async (memoryId: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete(`/api/memories/${memoryId}/like`)
    return response.data
  },

  // 북마크 추가
  addBookmark: async (memoryId: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.post(`/api/memories/${memoryId}/bookmark`)
    return response.data
  },

  // 북마크 제거
  removeBookmark: async (memoryId: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete(`/api/memories/${memoryId}/bookmark`)
    return response.data
  },

  // 북마크된 메모리 목록 조회
  getBookmarkedMemories: async (page: number = 0, size: number = 10): Promise<ApiResponse<MemoryListResponse>> => {
    const response = await apiClient.get('/api/memories/bookmarks', {
      params: { page, size }
    })
    return response.data
  },

  // 메모리 필터링
  filterMemories: async (request: MemoryFilterRequest): Promise<ApiResponse<MemoryListResponse>> => {
    const response = await apiClient.get('/api/memories/filter', {
      params: request
    })
    return response.data
  },

  // 추천 메모리 조회
  getRecommendedMemories: async (request: MemoryRecommendRequest): Promise<ApiResponse<MemoryListResponse>> => {
    const response = await apiClient.get('/api/memories/recommend', {
      params: request
    })
    return response.data
  },

  // 랜덤 메모리 조회
  getRandomMemories: async (emotion?: string, category?: string, count: number = 1): Promise<ApiResponse<any>> => {
    const response = await apiClient.get('/api/memories/random', {
      params: { emotion, category, count }
    })
    return response.data
  }
} 