import { apiClient } from './config'
import type { ApiResponse } from './config'

// 알림 응답 타입
export interface NotificationResponse {
  id: number
  type: string
  title: string
  content: string
  isRead: boolean
  relatedId?: number
  createdAt: string
  message: string
}

// 알림 API 함수들
export const notificationAPI = {
  // 알림 목록 조회
  getNotifications: async (): Promise<ApiResponse<NotificationResponse[]>> => {
    const response = await apiClient.get('/api/notifications')
    return response.data
  },

  // 알림 읽음 처리
  markAsRead: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.put(`/api/notifications/${id}/read`)
    return response.data
  },

  // 알림 삭제
  deleteNotification: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete(`/api/notifications/${id}`)
    return response.data
  },

  // 읽지 않은 알림 수 조회
  getUnreadCount: async (): Promise<ApiResponse<number>> => {
    const response = await apiClient.get('/api/notifications/unread-count')
    return response.data
  }
} 