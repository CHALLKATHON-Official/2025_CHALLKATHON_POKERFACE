import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiBell, FiHeart, FiUserPlus, FiMessageCircle, FiBookmark, FiTrash2, FiCheck } from 'react-icons/fi'
import { notificationAPI } from '../api/notification'
import type { NotificationResponse } from '../api/notification'
import './Notification.css'

const NOTIFICATION_ICONS = {
  like: FiHeart,
  follow: FiUserPlus,
  comment: FiMessageCircle,
  bookmark: FiBookmark,
  default: FiBell
}

export default function Notification() {
  const navigate = useNavigate()
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

  const handleNotificationClick = (notification: NotificationResponse) => {
    // 알림 타입에 따라 다른 페이지로 이동
    if (!notification.isRead) {
      handleMarkAsRead(notification.id)
    }

    switch (notification.type) {
      case 'like':
      case 'comment':
      case 'bookmark':
        if (notification.relatedId) {
          navigate(`/memory/${notification.relatedId}`)
        }
        break
      case 'follow':
        if (notification.relatedId) {
          navigate(`/profile/${notification.relatedId}`)
        }
        break
      default:
        break
    }
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    const now = new Date()
    const diffInHours = (now.getTime() - date.getTime()) / (1000 * 60 * 60)
    
    if (diffInHours < 1) {
      return '방금 전'
    } else if (diffInHours < 24) {
      return `${Math.floor(diffInHours)}시간 전`
    } else if (diffInHours < 24 * 7) {
      return `${Math.floor(diffInHours / 24)}일 전`
    } else {
      return date.toLocaleDateString('ko-KR')
    }
  }

  const getNotificationIcon = (type: string) => {
    const Icon = NOTIFICATION_ICONS[type as keyof typeof NOTIFICATION_ICONS] || NOTIFICATION_ICONS.default
    return <Icon size={20} />
  }

  const getNotificationColor = (type: string) => {
    switch (type) {
      case 'like':
        return '#FF6B6B'
      case 'follow':
        return '#4ECDC4'
      case 'comment':
        return '#45B7D1'
      case 'bookmark':
        return '#96CEB4'
      default:
        return '#B8B6FF'
    }
  }

  const unreadCount = notifications.filter(n => !n.isRead).length

  return (
    <div className="notification-container">
      <div className="notification-header">
        <h1>알림</h1>
        {unreadCount > 0 && (
          <div className="notification-badge badge-pastel">
            {unreadCount}
          </div>
        )}
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {isLoading ? (
        <div className="notification-loading">
          알림을 불러오는 중...
        </div>
      ) : notifications.length > 0 ? (
        <div className="notification-list">
          {notifications.map((notification) => (
            <div
              key={notification.id}
              className={`notification-item card-glass ${!notification.isRead ? 'unread' : ''}`}
              onClick={() => handleNotificationClick(notification)}
            >
              <div 
                className="notification-icon"
                style={{ color: getNotificationColor(notification.type) }}
              >
                {getNotificationIcon(notification.type)}
              </div>
              
              <div className="notification-content">
                <div className="notification-message">
                  {notification.message}
                </div>
                <div className="notification-time badge-pastel">
                  {formatDate(notification.createdAt)}
                </div>
              </div>
              
              <div className="notification-actions">
                {!notification.isRead && (
                  <button
                    className="notification-action-btn btn-main"
                    onClick={(e) => {
                      e.stopPropagation()
                      handleMarkAsRead(notification.id)
                    }}
                  >
                    <FiCheck size={16} />
                  </button>
                )}
                <button
                  className="notification-action-btn btn-main"
                  onClick={(e) => {
                    e.stopPropagation()
                    handleDeleteNotification(notification.id)
                  }}
                >
                  <FiTrash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="notification-empty card-glass">
          <FiBell size={48} />
          <p>새로운 알림이 없습니다.</p>
          <p>친구들과 소통해보세요!</p>
        </div>
      )}
    </div>
  )
} 