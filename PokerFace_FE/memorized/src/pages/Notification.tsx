import { useState, useEffect } from 'react'
import { notificationAPI } from '../api/notification'
import type { NotificationResponse } from '../api/notification'
import './Notification.css'

export default function Notification() {
  const [notifications, setNotifications] = useState<NotificationResponse[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    loadNotifications()
  }, [])

  const loadNotifications = async () => {
    setIsLoading(true)
    setError('')

    try {
      const response = await notificationAPI.getNotifications()
      if (response.success) {
        setNotifications(response.data)
      } else {
        setError('알림을 불러오는데 실패했습니다.')
      }
    } catch (err: any) {
      console.error('Load notifications error:', err)
      setError('알림을 불러오는 중 오류가 발생했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleMarkAsRead = async (id: number) => {
    try {
      await notificationAPI.markAsRead(id)
      setNotifications(prev => 
        prev.map(notification => 
          notification.id === id 
            ? { ...notification, isRead: true }
            : notification
        )
      )
    } catch (error) {
      console.error('Mark as read error:', error)
    }
  }

  const handleDeleteNotification = async (id: number) => {
    try {
      await notificationAPI.deleteNotification(id)
      setNotifications(prev => prev.filter(notification => notification.id !== id))
    } catch (error) {
      console.error('Delete notification error:', error)
    }
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    const now = new Date()
    const diffInHours = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60))
    
    if (diffInHours < 1) {
      return '방금 전'
    } else if (diffInHours < 24) {
      return `${diffInHours}시간 전`
    } else {
      const diffInDays = Math.floor(diffInHours / 24)
      return `${diffInDays}일 전`
    }
  }

  return (
    <div className="notification-container">
      <div className="notification-header">
        <h2>알림</h2>
      </div>

      {error && (
        <div className="notification-error">
          {error}
        </div>
      )}

      <div className="notification-list">
        {isLoading ? (
          <div className="notification-loading">알림을 불러오는 중...</div>
        ) : notifications.length > 0 ? (
          notifications.map((notification) => (
            <div 
              key={notification.id} 
              className={`notification-item ${!notification.isRead ? 'unread' : ''}`}
            >
              <div className="notification-content">
                <div className="notification-title">{notification.title}</div>
                <div className="notification-message">{notification.content}</div>
                <div className="notification-time">{formatDate(notification.createdAt)}</div>
              </div>
              <div className="notification-actions">
                {!notification.isRead && (
                  <button 
                    className="notification-read-btn"
                    onClick={() => handleMarkAsRead(notification.id)}
                  >
                    읽음
                  </button>
                )}
                <button 
                  className="notification-delete-btn"
                  onClick={() => handleDeleteNotification(notification.id)}
                >
                  삭제
                </button>
              </div>
            </div>
          ))
        ) : (
          <div className="notification-empty">
            새로운 알림이 없습니다.
          </div>
        )}
      </div>
    </div>
  )
} 