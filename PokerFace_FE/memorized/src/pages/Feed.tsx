import { useState, useEffect, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiHeart, FiBookmark, FiMessageCircle, FiMoreHorizontal, FiTrash2, FiEdit } from 'react-icons/fi'
import { memoryAPI } from '../api/memory'
import type { Memory } from '../api/memory'
import { useAuth } from '../contexts/AuthContext'
import './Feed.css'

export default function Feed() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [memories, setMemories] = useState<Memory[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [hasNext, setHasNext] = useState(true)
  const [currentPage, setCurrentPage] = useState(0)
  const [showMenuId, setShowMenuId] = useState<number | null>(null)
  const observerRef = useRef<IntersectionObserver>()
  const loadingRef = useRef<HTMLDivElement>(null)
  const hasNextRef = useRef(hasNext)
  const isLoadingRef = useRef(isLoading)
  const currentPageRef = useRef(currentPage)

  const loadMemories = useCallback(async (page: number = 0) => {
    if (isLoading) return
    setIsLoading(true)
    try {
      const response = await memoryAPI.getMemories('all', page, 10)
      if (response.success) {
        if (page === 0) {
          setMemories(response.data.memories)
        } else {
          setMemories(prev => [...prev, ...response.data.memories])
        }
        setHasNext(page < response.data.totalPages - 1)
        setCurrentPage(page)
      }
    } catch (error) {
      console.error('Failed to load memories:', error)
    } finally {
      setIsLoading(false)
    }
  }, [isLoading])

  // 초기 로드
  useEffect(() => {
    loadMemories(0)
    // eslint-disable-next-line
  }, [])

  useEffect(() => {
    hasNextRef.current = hasNext
    isLoadingRef.current = isLoading
    currentPageRef.current = currentPage
  }, [hasNext, isLoading, currentPage])

  // 무한 스크롤 설정
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNextRef.current && !isLoadingRef.current) {
          loadMemories(currentPageRef.current + 1)
        }
      },
      { threshold: 0.1 }
    )
    observerRef.current = observer

    if (loadingRef.current) {
      observer.observe(loadingRef.current)
    }

    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect()
      }
    }
  }, [])

  // 좋아요 토글
  const handleLikeToggle = async (memoryId: number) => {
    try {
      const memory = memories.find(m => m.id === memoryId)
      if (!memory) return

      if (memory.isLiked) {
        await memoryAPI.removeLike(memoryId)
        setMemories(prev => prev.map(m => 
          m.id === memoryId 
            ? { ...m, isLiked: false, likeCount: m.likeCount - 1 }
            : m
        ))
      } else {
        await memoryAPI.addLike(memoryId)
        setMemories(prev => prev.map(m => 
          m.id === memoryId 
            ? { ...m, isLiked: true, likeCount: m.likeCount + 1 }
            : m
        ))
      }
    } catch (error) {
      console.error('Like toggle error:', error)
    }
  }

  // 북마크 토글
  const handleBookmarkToggle = async (memoryId: number) => {
    try {
      const memory = memories.find(m => m.id === memoryId)
      if (!memory) return

      if (memory.isBookmarked) {
        await memoryAPI.removeBookmark(memoryId)
        setMemories(prev => prev.map(m => 
          m.id === memoryId 
            ? { ...m, isBookmarked: false, bookmarkCount: m.bookmarkCount - 1 }
            : m
        ))
      } else {
        await memoryAPI.addBookmark(memoryId)
        setMemories(prev => prev.map(m => 
          m.id === memoryId 
            ? { ...m, isBookmarked: true, bookmarkCount: m.bookmarkCount + 1 }
            : m
        ))
      }
    } catch (error) {
      console.error('Bookmark toggle error:', error)
    }
  }

  // 메모리 삭제
  const handleDeleteMemory = async (memoryId: number) => {
    if (!window.confirm('정말로 이 메모리를 삭제하시겠습니까?')) return
    
    try {
      await memoryAPI.deleteMemory(memoryId)
      setMemories(prev => prev.filter(m => m.id !== memoryId))
      setShowMenuId(null)
    } catch (error) {
      console.error('Delete memory error:', error)
      alert('메모리 삭제에 실패했습니다.')
    }
  }

  // 메모리 수정
  const handleEditMemory = (memoryId: number) => {
    navigate(`/edit/${memoryId}`)
    setShowMenuId(null)
  }

  // 날짜 포맷팅
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

  // 프로필 아이콘 SVG
  const ProfileIcon = () => (
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="20" cy="20" r="20" fill="#e3e6ee"/>
      <circle cx="20" cy="16" r="7" fill="#b8b6ff"/>
      <ellipse cx="20" cy="30" rx="11" ry="7" fill="#b8b6ff"/>
    </svg>
  )

  return (
    <div className="feed-container">
      {memories.map((memory) => (
        <div key={memory.id} className="memory-card card-glass">
          <div className="memory-header">
            <div className="memory-user">
              <div className="memory-avatar">
                {memory.userProfileImage ? (
                  <img src={memory.userProfileImage} alt={memory.username} />
                ) : (
                  <ProfileIcon />
                )}
              </div>
              <div className="memory-user-info">
                <div className="memory-username">{memory.username}</div>
                <div className="memory-date">{formatDate(memory.createdAt)}</div>
              </div>
            </div>
            
            {/* 메뉴 버튼 (본인 글인 경우에만) */}
            {user && memory.userId === user.id && (
              <div className="memory-menu">
                <button className="memory-menu-btn btn-main" onClick={() => setShowMenuId(showMenuId === memory.id ? null : memory.id)}>
                  <FiMoreHorizontal size={20} />
                </button>
                {showMenuId === memory.id && (
                  <div className="memory-menu-dropdown card-glass">
                    <button onClick={() => handleEditMemory(memory.id)}>
                      <FiEdit size={16} /> 수정
                    </button>
                    <button onClick={() => handleDeleteMemory(memory.id)}>
                      <FiTrash2 size={16} /> 삭제
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>

          <div className="memory-content">
            <p>{memory.content}</p>
            {memory.imageUrl && (
              <div className="memory-image">
                <img src={memory.imageUrl} alt="메모리 이미지" />
              </div>
            )}
            {memory.emotion && (
              <div className="memory-emotion badge-pastel">
                감정: {memory.emotion}
              </div>
            )}
          </div>

          <div className="memory-actions">
            <button 
              className={`memory-action-btn btn-main ${memory.isLiked ? 'liked' : ''}`}
              onClick={() => handleLikeToggle(memory.id)}
            >
              <FiHeart size={18} />
              <span>{memory.likeCount}</span>
            </button>
            
            <button className="memory-action-btn btn-main">
              <FiMessageCircle size={18} />
              <span>댓글</span>
            </button>
            
            <button 
              className={`memory-action-btn btn-main ${memory.isBookmarked ? 'bookmarked' : ''}`}
              onClick={() => handleBookmarkToggle(memory.id)}
            >
              <FiBookmark size={18} />
              <span>{memory.bookmarkCount}</span>
            </button>
          </div>
        </div>
      ))}

      {hasNext && (
        <div ref={loadingRef} className="loading-indicator">
          {isLoading ? '로딩 중...' : ''}
        </div>
      )}
    </div>
  )
} 