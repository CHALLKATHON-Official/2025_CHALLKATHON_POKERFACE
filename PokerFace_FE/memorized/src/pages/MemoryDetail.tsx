import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { memoryAPI } from '../api/memory'
import type { MemoryDetailResponse } from '../api/memory'
import { FiArrowLeft, FiUser } from 'react-icons/fi'
import './Feed.css'
import './Write.css'
import './MemoryDetail.css'

export default function MemoryDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [memory, setMemory] = useState<MemoryDetailResponse | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!id) return
    setIsLoading(true)
    memoryAPI.getMemoryDetail(Number(id))
      .then(res => {
        if ('success' in res && res.success) setMemory(res.data)
        else if ('id' in res) setMemory(res as any)
        else setError('메모리를 불러오지 못했습니다.')
      })
      .catch(() => setError('메모리를 불러오지 못했습니다.'))
      .finally(() => setIsLoading(false))
  }, [id])

  if (isLoading) return <div className="feed-loading">로딩 중...</div>
  if (error || !memory) return (
    <div className="feed-error card-glass">
      <p>{error || '메모리를 찾을 수 없습니다.'}</p>
      <button className="btn-main" onClick={() => navigate(-1)}>돌아가기</button>
    </div>
  )

  return (
    <div className="memory-detail-bg">
      <div className="memory-detail-card">
        <div className="memory-detail-header">
          <button className="btn-main memory-detail-back" onClick={() => navigate(-1)}>
            <FiArrowLeft size={18} /> 뒤로가기
          </button>
          <div className="memory-detail-profile">
            {memory.profileImageUrl ? (
              <img src={memory.profileImageUrl} alt="프로필" className="memory-detail-avatar" />
            ) : (
              <div style={{ width: 48, height: 48, borderRadius: '50%', background: '#e3e6ee', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <FiUser size={32} />
              </div>
            )}
            <div className="memory-detail-userinfo">
              <div className="memory-detail-username">{memory.username || `작성자 ID: ${memory.userId}`}</div>
              <div className="memory-detail-date">{new Date(memory.createdAt).toLocaleDateString('ko-KR')}</div>
            </div>
          </div>
        </div>
        {memory.imageUrl && (
          <div className="memory-detail-image">
            <img src={memory.imageUrl} alt="메모리 이미지" />
          </div>
        )}
        <div className="memory-detail-content">{memory.content}</div>
        <div className="memory-detail-actions">
          <span className="memory-detail-like">
            <svg width="22" height="22" fill="none" stroke="#e57373" strokeWidth="2" viewBox="0 0 24 24"><path d="M12 21C12 21 4 13.5 4 8.5C4 5.42 6.42 3 9.5 3C11.24 3 12.91 3.81 14 5.08C15.09 3.81 16.76 3 18.5 3C21.58 3 24 5.42 24 8.5C24 13.5 16 21 16 21H12Z"/></svg>
            {memory.likes}
          </span>
          <span className="memory-detail-comment">
            <svg width="22" height="22" fill="none" stroke="#64b5f6" strokeWidth="2" viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
            {memory.comments?.length ?? 0}
          </span>
        </div>
      </div>
    </div>
  )
} 