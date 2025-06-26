import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { FiEdit3, FiTrash2, FiHeart, FiMessageCircle, FiImage, FiMusic, FiBook, FiCalendar, FiMapPin, FiUsers, FiStar, FiTrendingUp } from 'react-icons/fi'
import { useAuth } from '../contexts/AuthContext'
import './MyRoom.css'

interface User {
  id: number
  username: string
  email: string
  profileImageUrl?: string
  bio?: string
  joinDate: string
  memoryCount: number
  followerCount: number
  followingCount: number
}

interface GuestbookEntry {
  id: number
  content: string
  author: string
  authorId: number
  createdAt: string
  isAuthor: boolean
}

interface Memory {
  id: number
  content: string
  emotion?: string
  imageUrl?: string
  createdAt: string
  likeCount: number
}

interface RoomStats {
  totalMemories: number
  totalLikes: number
  totalViews: number
  averageEmotion: string
  mostActiveDay: string
  recentActivity: string
}

// ProfileIcon 컴포넌트 정의
const ProfileIcon = () => (
  <svg width="100%" height="100%" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="20" cy="20" r="20" fill="#e3e6ee"/>
    <circle cx="20" cy="16" r="7" fill="#b8b6ff"/>
    <ellipse cx="20" cy="30" rx="11" ry="7" fill="#b8b6ff"/>
  </svg>
)

export default function MyRoom() {
  const { username } = useParams<{ username: string }>()
  const navigate = useNavigate()
  const { user: currentUser } = useAuth()
  
  const [user, setUser] = useState<User | null>(null)
  const [guestbook, setGuestbook] = useState<GuestbookEntry[]>([])
  const [memories, setMemories] = useState<Memory[]>([])
  const [stats, setStats] = useState<RoomStats | null>(null)
  const [isFriend, setIsFriend] = useState(false)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  
  // 방명록 관련 상태
  const [newGuestbookEntry, setNewGuestbookEntry] = useState('')
  const [isWritingGuestbook, setIsWritingGuestbook] = useState(false)
  
  // 프로필 편집 관련 상태
  const [isEditingProfile, setIsEditingProfile] = useState(false)
  const [editForm, setEditForm] = useState({
    bio: '',
    theme: 'default'
  })

  useEffect(() => {
    if (username) {
      loadRoomData()
    }
  }, [username])

  const loadRoomData = async () => {
    try {
      setIsLoading(true)
      
      // 임시 사용자 데이터 (실제로는 API 호출)
      const mockUser: User = {
        id: 1,
        username: username || 'user',
        email: 'user@example.com',
        bio: '시간을 수집하는 사람입니다.',
        joinDate: '2024-01-01',
        memoryCount: 42,
        followerCount: 15,
        followingCount: 12
      }
      setUser(mockUser)
      
      // 임시 방명록 데이터
      const mockGuestbook: GuestbookEntry[] = [
        {
          id: 1,
          content: '방문했어요! 정말 예쁜 미니홈피네요 😊',
          author: '친구1',
          authorId: 2,
          createdAt: '2025-01-15T10:30:00Z',
          isAuthor: false
        },
        {
          id: 2,
          content: '오늘도 좋은 하루 되세요~',
          author: '친구2',
          authorId: 3,
          createdAt: '2025-01-14T15:20:00Z',
          isAuthor: false
        }
      ]
      setGuestbook(mockGuestbook)
      
      // 임시 메모리 데이터
      const mockMemories: Memory[] = [
        {
          id: 1,
          content: '오늘은 정말 좋은 날씨였어요. 산책하면서 느낀 평화로움을 기록해봅니다.',
          emotion: 'HAPPY',
          createdAt: '2025-01-15T14:30:00Z',
          likeCount: 5
        },
        {
          id: 2,
          content: '새로운 책을 읽기 시작했어요. 기대가 됩니다!',
          emotion: 'EXCITED',
          createdAt: '2025-01-14T20:15:00Z',
          likeCount: 3
        }
      ]
      setMemories(mockMemories)
      
      // 임시 통계 데이터
      const mockStats: RoomStats = {
        totalMemories: 42,
        totalLikes: 156,
        totalViews: 1234,
        averageEmotion: '행복',
        mostActiveDay: '토요일',
        recentActivity: '2시간 전'
      }
      setStats(mockStats)
      
      // 일촌 상태 (임시)
      setIsFriend(false)
      
    } catch (error) {
      console.error('방 정보 로드 실패:', error)
      setError('방 정보를 불러오는데 실패했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleGuestbookSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!newGuestbookEntry.trim()) return
    
    try {
      // 임시 방명록 추가 (실제로는 API 호출)
      const newEntry: GuestbookEntry = {
        id: Date.now(),
        content: newGuestbookEntry,
        author: currentUser?.username || '익명',
        authorId: currentUser?.id || 0,
        createdAt: new Date().toISOString(),
        isAuthor: true
      }
      
      setGuestbook(prev => [newEntry, ...prev])
      setNewGuestbookEntry('')
      setIsWritingGuestbook(false)
    } catch (error) {
      console.error('방명록 작성 실패:', error)
      alert('방명록 작성에 실패했습니다.')
    }
  }

  const handleGuestbookDelete = async (entryId: number) => {
    if (!confirm('방명록을 삭제하시겠습니까?')) return
    
    try {
      // 임시 방명록 삭제 (실제로는 API 호출)
      setGuestbook(prev => prev.filter(entry => entry.id !== entryId))
    } catch (error) {
      console.error('방명록 삭제 실패:', error)
      alert('방명록 삭제에 실패했습니다.')
    }
  }

  const handleFriendToggle = async () => {
    if (!currentUser) {
      alert('로그인이 필요합니다.')
      return
    }
    
    try {
      // 임시 일촌 토글 (실제로는 API 호출)
      setIsFriend(!isFriend)
      alert(isFriend ? '일촌을 끊었습니다.' : '일촌을 맺었습니다.')
    } catch (error) {
      console.error('일촌 관리 실패:', error)
      alert('일촌 관리에 실패했습니다.')
    }
  }

  const handleProfileEdit = async () => {
    try {
      // 임시 프로필 수정 (실제로는 API 호출)
      setUser(prev => prev ? { ...prev, bio: editForm.bio } : null)
      setIsEditingProfile(false)
      alert('프로필이 수정되었습니다.')
    } catch (error) {
      console.error('프로필 수정 실패:', error)
      alert('프로필 수정에 실패했습니다.')
    }
  }

  if (isLoading) {
    return (
      <div className="myroom-container">
        <div className="myroom-loading card-glass">
          <div className="loading-spinner"></div>
          <p>방 정보를 불러오는 중...</p>
        </div>
      </div>
    )
  }

  if (error || !user) {
    return (
      <div className="myroom-container">
        <div className="myroom-error card-glass">
          <h2>방을 찾을 수 없습니다</h2>
          <p>{error || '존재하지 않는 사용자입니다.'}</p>
          <button className="btn-main" onClick={() => navigate('/')}>
            홈으로 돌아가기
          </button>
        </div>
      </div>
    )
  }

  const isOwnRoom = currentUser?.username === username

  return (
    <div className="myroom-container">
      {/* 프로필 헤더 */}
      <div className="myroom-profile-header card-glass">
        <div className="profile-avatar-section">
          <div className="profile-avatar">
            {user.profileImageUrl ? (
              <img src={user.profileImageUrl} alt={user.username} />
            ) : (
              <ProfileIcon />
            )}
          </div>
          {isOwnRoom && (
            <button 
              className="profile-edit-btn btn-main"
              onClick={() => setIsEditingProfile(true)}
            >
              <FiEdit3 size={16} /> 프로필 편집
            </button>
          )}
        </div>
        
        <div className="profile-info">
          <h1 className="profile-username">{user.username}</h1>
          {user.bio && <p className="profile-bio">{user.bio}</p>}
          <div className="profile-meta">
            <span className="profile-join-date">
              <FiCalendar size={14} /> {new Date(user.joinDate).toLocaleDateString()} 가입
            </span>
            <span className="profile-location">
              <FiMapPin size={14} /> 서울
            </span>
          </div>
        </div>
        
        {!isOwnRoom && currentUser && (
          <div className="profile-actions">
            <button 
              className={`friend-btn btn-main ${isFriend ? 'unfriend' : ''}`}
              onClick={handleFriendToggle}
            >
              {isFriend ? '일촌 끊기' : '일촌 맺기'}
            </button>
            <button className="message-btn btn-main">
              <FiMessageCircle size={16} /> 메시지
            </button>
          </div>
        )}
      </div>

      {/* 통계 대시보드 */}
      {stats && (
        <div className="myroom-stats card-glass">
          <h3 className="stats-title">
            <FiTrendingUp size={20} /> 활동 통계
          </h3>
          <div className="stats-grid">
            <div className="stat-item">
              <div className="stat-icon">📝</div>
              <div className="stat-content">
                <div className="stat-value">{stats.totalMemories}</div>
                <div className="stat-label">총 메모리</div>
              </div>
            </div>
            <div className="stat-item">
              <div className="stat-icon">❤️</div>
              <div className="stat-content">
                <div className="stat-value">{stats.totalLikes}</div>
                <div className="stat-label">받은 좋아요</div>
              </div>
            </div>
            <div className="stat-item">
              <div className="stat-icon">👁️</div>
              <div className="stat-content">
                <div className="stat-value">{stats.totalViews}</div>
                <div className="stat-label">방문자</div>
              </div>
            </div>
            <div className="stat-item">
              <div className="stat-icon">😊</div>
              <div className="stat-content">
                <div className="stat-value">{stats.averageEmotion}</div>
                <div className="stat-label">주요 감정</div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* 메뉴 섹션 */}
      <div className="myroom-menu card-glass">
        <div className="menu-item" onClick={() => navigate(`/user/${username}/memories`)}>
          <div className="menu-icon">📝</div>
          <span>메모리</span>
          <span className="menu-count badge-pastel">{user.memoryCount}</span>
        </div>
        <div className="menu-item" onClick={() => setIsWritingGuestbook(true)}>
          <div className="menu-icon">💬</div>
          <span>방명록</span>
          <span className="menu-count badge-pastel">{guestbook.length}</span>
        </div>
        <div className="menu-item">
          <div className="menu-icon">📷</div>
          <span>사진첩</span>
          <span className="menu-count badge-pastel">24</span>
        </div>
        <div className="menu-item">
          <div className="menu-icon">🎵</div>
          <span>음악</span>
          <span className="menu-count badge-pastel">12</span>
        </div>
        <div className="menu-item">
          <div className="menu-icon">📚</div>
          <span>독서록</span>
          <span className="menu-count badge-pastel">8</span>
        </div>
        <div className="menu-item">
          <div className="menu-icon">👥</div>
          <span>일촌</span>
          <span className="menu-count badge-pastel">{user.followerCount}</span>
        </div>
      </div>

      {/* 최근 메모리 */}
      {memories.length > 0 && (
        <div className="myroom-recent-memories card-glass">
          <h3 className="section-title">
            <FiStar size={20} /> 최근 메모리
          </h3>
          <div className="memories-grid">
            {memories.slice(0, 3).map((memory) => (
              <div key={memory.id} className="memory-preview">
                <div className="memory-content">
                  <p>{memory.content.length > 50 ? `${memory.content.substring(0, 50)}...` : memory.content}</p>
                  {memory.imageUrl && (
                    <div className="memory-image">
                      <img src={memory.imageUrl} alt="메모리 이미지" />
                    </div>
                  )}
                </div>
                <div className="memory-meta">
                  <span className="memory-date">
                    {new Date(memory.createdAt).toLocaleDateString()}
                  </span>
                  {memory.emotion && (
                    <span className="memory-emotion badge-pastel">
                      {memory.emotion}
                    </span>
                  )}
                  <span className="memory-likes">
                    <FiHeart size={12} /> {memory.likeCount}
                  </span>
                </div>
              </div>
            ))}
          </div>
          <button 
            className="view-all-btn btn-main"
            onClick={() => navigate(`/user/${username}/memories`)}
          >
            모든 메모리 보기
          </button>
        </div>
      )}

      {/* 방명록 섹션 */}
      <div className="myroom-guestbook card-glass">
        <div className="guestbook-header">
          <h3 className="section-title">
            <FiMessageCircle size={20} /> 방명록
          </h3>
          {currentUser && (
            <button 
              className="write-guestbook-btn btn-main"
              onClick={() => setIsWritingGuestbook(true)}
            >
              방명록 쓰기
            </button>
          )}
        </div>
        
        {guestbook.length > 0 ? (
          <div className="guestbook-list">
            {guestbook.map((entry) => (
              <div key={entry.id} className="guestbook-entry">
                <div className="entry-header">
                  <span className="entry-author">{entry.author}</span>
                  <span className="entry-date">
                    {new Date(entry.createdAt).toLocaleDateString()}
                  </span>
                  {entry.isAuthor && (
                    <button
                      className="entry-delete-btn btn-main"
                      onClick={() => handleGuestbookDelete(entry.id)}
                    >
                      <FiTrash2 size={14} />
                    </button>
                  )}
                </div>
                <p className="entry-content">{entry.content}</p>
              </div>
            ))}
          </div>
        ) : (
          <div className="guestbook-empty">
            <p>아직 방명록이 없습니다.</p>
            {currentUser && (
              <button 
                className="write-first-guestbook-btn btn-main"
                onClick={() => setIsWritingGuestbook(true)}
              >
                첫 방명록 남기기
              </button>
            )}
          </div>
        )}
      </div>

      {/* 방명록 작성 모달 */}
      {isWritingGuestbook && (
        <div className="guestbook-modal card-glass">
          <div className="modal-header">
            <h3>방명록 작성</h3>
            <button 
              className="modal-close-btn btn-main"
              onClick={() => {
                setIsWritingGuestbook(false)
                setNewGuestbookEntry('')
              }}
            >
              ✕
            </button>
          </div>
          <form onSubmit={handleGuestbookSubmit} className="guestbook-form">
            <textarea
              className="guestbook-textarea input-glass"
              placeholder={`${user.username}님에게 메시지를 남겨보세요...`}
              value={newGuestbookEntry}
              onChange={(e) => setNewGuestbookEntry(e.target.value)}
              maxLength={200}
            />
            <div className="guestbook-char-count badge-pastel">
              {newGuestbookEntry.length}/200
            </div>
            <div className="guestbook-actions">
              <button
                type="button"
                className="guestbook-cancel-btn btn-main"
                onClick={() => {
                  setIsWritingGuestbook(false)
                  setNewGuestbookEntry('')
                }}
              >
                취소
              </button>
              <button
                type="submit"
                className="guestbook-submit-btn btn-main"
                disabled={!newGuestbookEntry.trim()}
              >
                방명록 남기기
              </button>
            </div>
          </form>
        </div>
      )}

      {/* 프로필 편집 모달 */}
      {isEditingProfile && (
        <div className="profile-edit-modal card-glass">
          <div className="modal-header">
            <h3>프로필 편집</h3>
            <button 
              className="modal-close-btn btn-main"
              onClick={() => setIsEditingProfile(false)}
            >
              ✕
            </button>
          </div>
          <form onSubmit={(e) => { e.preventDefault(); handleProfileEdit(); }} className="profile-edit-form">
            <div className="form-group">
              <label>소개글</label>
              <textarea
                className="profile-bio-input input-glass"
                placeholder="자신을 소개해보세요..."
                value={editForm.bio}
                onChange={(e) => setEditForm(prev => ({ ...prev, bio: e.target.value }))}
                maxLength={100}
              />
              <div className="char-count badge-pastel">
                {editForm.bio.length}/100
              </div>
            </div>
            <div className="form-group">
              <label>테마 선택</label>
              <select
                className="theme-select input-glass"
                value={editForm.theme}
                onChange={(e) => setEditForm(prev => ({ ...prev, theme: e.target.value }))}
              >
                <option value="default">기본 테마</option>
                <option value="spring">봄 테마</option>
                <option value="summer">여름 테마</option>
                <option value="autumn">가을 테마</option>
                <option value="winter">겨울 테마</option>
              </select>
            </div>
            <div className="profile-edit-actions">
              <button
                type="button"
                className="profile-cancel-btn btn-main"
                onClick={() => setIsEditingProfile(false)}
              >
                취소
              </button>
              <button
                type="submit"
                className="profile-save-btn btn-main"
              >
                저장
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  )
} 