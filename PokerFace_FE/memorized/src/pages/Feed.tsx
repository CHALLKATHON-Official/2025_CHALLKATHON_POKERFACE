import { useState, useEffect, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiHeart, FiBookmark, FiMessageCircle, FiMoreHorizontal } from 'react-icons/fi'
import { memoryAPI } from '../api/memory'
import type { Memory } from '../api/memory'
import './Feed.css'

export default function Feed() {
  const navigate = useNavigate()
  const [memories, setMemories] = useState<Memory[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [hasNext, setHasNext] = useState(true)
  const [currentPage, setCurrentPage] = useState(0)
  const observerRef = useRef<IntersectionObserver>()
  const loadingRef = useRef<HTMLDivElement>(null)

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

  // 무한 스크롤 설정
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext && !isLoading) {
          loadMemories(currentPage + 1)
        }
      },
      { threshold: 0.1 }
    )
    
    if (loadingRef.current) {
      observer.observe(loadingRef.current)
    }
    
    observerRef.current = observer
    
    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect()
      }
    }
  }, [hasNext, isLoading, currentPage, loadMemories])

  // 좋아요 토글
  const handleLikeToggle = async (memoryId: number, isLiked: boolean) => {
    try {
      if (isLiked) {
        await memoryAPI.removeLike(memoryId)
      } else {
        await memoryAPI.addLike(memoryId)
      }
      
      // 메모리 목록 업데이트
      setMemories(prev => prev.map(memory => 
        memory.id === memoryId 
          ? { 
              ...memory, 
              isLiked: !isLiked,
              likeCount: isLiked ? memory.likeCount - 1 : memory.likeCount + 1
            }
          : memory
      ))
    } catch (error) {
      console.error('Failed to toggle like:', error)
    }
  }

  // 북마크 토글
  const handleBookmarkToggle = async (memoryId: number, isBookmarked: boolean) => {
    try {
      if (isBookmarked) {
        await memoryAPI.removeBookmark(memoryId)
      } else {
        await memoryAPI.addBookmark(memoryId)
      }
      
      // 메모리 목록 업데이트
      setMemories(prev => prev.map(memory => 
        memory.id === memoryId 
          ? { 
              ...memory, 
              isBookmarked: !isBookmarked,
              bookmarkCount: isBookmarked ? memory.bookmarkCount - 1 : memory.bookmarkCount + 1
            }
          : memory
      ))
    } catch (error) {
      console.error('Failed to toggle bookmark:', error)
    }
  }

  // 날짜 포맷팅
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
    <div className="feed-container">
      <div className="feed-content">
        {memories.map((memory) => (
          <article key={memory.id} className="memory-card">
            <div className="memory-header">
              <div className="memory-user-info" onClick={() => navigate(`/profile/${memory.userId}`)}>
                <img 
                  src={memory.userProfileImage || `https://i.pravatar.cc/40?img=${memory.userId}`} 
                  alt={memory.username}
                  className="memory-user-avatar"
                />
                <div className="memory-user-details">
                  <span className="memory-username">{memory.username}</span>
                  <span className="memory-date">{formatDate(memory.createdAt)}</span>
                </div>
              </div>
              <button className="memory-more-btn">
                <FiMoreHorizontal size={20} />
              </button>
            </div>
            
            <div className="memory-content">
              <p className="memory-text">{memory.content}</p>
              {memory.imageUrl && (
                <div className="memory-image-container">
                  <img 
                    src={memory.imageUrl} 
                    alt="Memory" 
                    className="memory-image"
                  />
                </div>
              )}
            </div>
            
            <div className="memory-actions">
              <div className="memory-action-left">
                <button 
                  className={`memory-action-btn ${memory.isLiked ? 'liked' : ''}`}
                  onClick={() => handleLikeToggle(memory.id, memory.isLiked)}
                >
                  <FiHeart size={20} />
                  <span>{memory.likeCount}</span>
                </button>
                <button className="memory-action-btn">
                  <FiMessageCircle size={20} />
                  <span>0</span>
                </button>
              </div>
              <button 
                className={`memory-action-btn ${memory.isBookmarked ? 'bookmarked' : ''}`}
                onClick={() => handleBookmarkToggle(memory.id, memory.isBookmarked)}
              >
                <FiBookmark size={20} />
                <span>{memory.bookmarkCount}</span>
              </button>
            </div>
          </article>
        ))}
        
        {hasNext && (
          <div ref={loadingRef} className="loading-indicator">
            {isLoading ? '로딩 중...' : ''}
          </div>
        )}
        
        {!hasNext && memories.length > 0 && (
          <div className="end-message">
            모든 메모리를 불러왔습니다.
          </div>
        )}
      </div>
    </div>
  )
} 