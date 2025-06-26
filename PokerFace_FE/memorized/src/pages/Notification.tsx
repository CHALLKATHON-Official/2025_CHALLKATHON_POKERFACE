import './Notification.css'
import { useEffect, useState } from 'react'
import axios from 'axios'
import type { NotificationItem } from './types'

export default function Notification() {
  const [notifications, setNotifications] = useState<NotificationItem[] | null>(null)
  const [unreadCount, setUnreadCount] = useState<number>(0)
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  // 🔹 알림 불러오기
const fetchNotifications = async () => {
  try {
    const response = await axios.get('/api/notifications')
    // 🔥 response.data.notifications 인지 확인
    const data = Array.isArray(response.data)
      ? response.data
      : response.data.notifications ?? []

    setNotifications(data)
  } catch (error) {
    console.error('알림 불러오기 실패', error)
    setNotifications([])
    setError('알림을 불러오지 못했습니다.')
  } finally {
    setLoading(false)
  }
}


  // 🔹 읽지 않은 알림 수 불러오기
  const fetchUnreadCount = async () => {
    try {
      const response = await axios.get<{ count: number }>('/api/notifications/unread-count')
      setUnreadCount(response.data.count ?? 0)
    } catch (error) {
      console.error('읽지 않은 알림 수 불러오기 실패', error)
    }
  }

  useEffect(() => {
    fetchNotifications()
    fetchUnreadCount()
  }, [])

  // 🔹 알림 읽음 처리
  const markAsRead = async (id: number) => {
    try {
      await axios.put(`/api/notifications/${id}/read`)
      fetchNotifications()
      fetchUnreadCount()
    } catch (error) {
      console.error('읽음 처리 실패', error)
    }
  }

  // 🔹 알림 삭제
  const deleteNotification = async (id: number) => {
    const confirmed = window.confirm('정말로 이 알림을 삭제하시겠습니까?')
    if (!confirmed) return

    try {
      await axios.delete(`/api/notifications/${id}`)
      fetchNotifications()
      fetchUnreadCount()
    } catch (error) {
      console.error('삭제 실패', error)
    }
  }

  if (loading) {
    return <div className="card-glass search-card fade-in">로딩 중...</div>
  }

  return (
    <div className="card-glass search-card fade-in">
      <div className="search-title">
        <span>알림 {unreadCount > 0 && `(${unreadCount}개 읽지 않음)`}</span>
      </div>
      <div className="search-section">
        <div className="search-section-title">최근 알림</div>

        {error && <div className="error-message">{error}</div>}

        <ul className="notice-list">
          {notifications && notifications.length === 0 ? (
            <li className="notice-empty">알림이 없습니다.</li>
          ) : (
            notifications &&
            notifications.map((notice) => (
              <li
                key={notice.id}
                className={`notice-item ${!notice.read ? 'unread' : ''}`}
                onClick={() => markAsRead(notice.id)}
              >
                {notice.message}
                <button
                  className="delete-button"
                  onClick={(e) => {
                    e.stopPropagation()
                    deleteNotification(notice.id)
                  }}
                >
                  ✖
                </button>
              </li>
            ))
          )}
        </ul>
      </div>
    </div>
  )
}
