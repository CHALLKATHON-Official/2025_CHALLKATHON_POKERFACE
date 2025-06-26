import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiEdit3, FiSettings, FiLogOut, FiUser, FiHeart, FiBookmark, FiImage } from 'react-icons/fi'
import { useAuth } from '../contexts/AuthContext'
import { authAPI } from '../api/auth'
import { memoryAPI } from '../api/memory'
import type { Memory } from '../api/memory'
import './MyPage.css'

export default function MyPage() {
  const navigate = useNavigate()
  const { user, logout } = useAuth()
  const [activeTab, setActiveTab] = useState('memories')
  const [memories, setMemories] = useState<Memory[]>([])
  const [bookmarks, setBookmarks] = useState<Memory[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [isEditing, setIsEditing] = useState(false)
  const [editForm, setEditForm] = useState({
    username: user?.username || '',
    email: user?.email || ''
  })
  const [stats, setStats] = useState({
    totalMemories: 0,
    totalLikes: 0,
    totalBookmarks: 0
  })

  useEffect(() => {
    if (user) {
      loadUserData()
    }
  }, [user])

  const loadUserData = async () => {
    setIsLoading(true)
    try {
      // 사용자 메모리 로드
      const memoriesResponse = await memoryAPI.getMemories('user', 0, 50)
      if (memoriesResponse.success) {
        setMemories(memoriesResponse.data.memories)
        setStats(prev => ({
          ...prev,
          totalMemories: memoriesResponse.data.totalElements
        }))
      }

      // 북마크된 메모리 로드
      const bookmarksResponse = await memoryAPI.getBookmarkedMemories(0, 50)
      if (bookmarksResponse.success) {
        setBookmarks(bookmarksResponse.data.memories)
        setStats(prev => ({
          ...prev,
          totalBookmarks: bookmarksResponse.data.totalElements
        }))
      }

      // 총 좋아요 수 계산
      const totalLikes = memoriesResponse.success 
        ? memoriesResponse.data.memories.reduce((sum, memory) => sum + memory.likeCount, 0)
        : 0
      setStats(prev => ({
        ...prev,
        totalLikes
      }))

    } catch (error) {
      console.error('Load user data error:', error)
    } finally {
      setIsLoading(false)
    }
  }

  const handleLogout = () => {
    if (window.confirm('정말로 로그아웃하시겠습니까?')) {
      logout()
      navigate('/login')
    }
  }

  const handleEditSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!editForm.username.trim() || !editForm.email.trim()) {
      alert('모든 필드를 입력해주세요.')
      return
    }

    try {
      const response = await authAPI.updateProfile({
        newUsername: editForm.username.trim(),
        profileImageUrl: user?.profileImageUrl || ''
      })
      
      if (response.success) {
        alert('프로필이 업데이트되었습니다.')
        setIsEditing(false)
        // 사용자 정보 새로고침
        window.location.reload()
      } else {
        alert('프로필 업데이트에 실패했습니다.')
      }
    } catch (error) {
      console.error('Update profile error:', error)
      alert('프로필 업데이트 중 오류가 발생했습니다.')
    }
  }

  const handleEditCancel = () => {
    setEditForm({
      username: user?.username || '',
      email: user?.email || ''
    })
    setIsEditing(false)
  }

  const handleMemoryClick = (memoryId: number) => {
    navigate(`/memory/${memoryId}`)
  }

  const handleDeleteMemory = async (memoryId: number) => {
    if (!window.confirm('정말로 이 메모리를 삭제하시겠습니까?')) return
    
    try {
      await memoryAPI.deleteMemory(memoryId)
      setMemories(prev => prev.filter(m => m.id !== memoryId))
      setStats(prev => ({ ...prev, totalMemories: prev.totalMemories - 1 }))
    } catch (error) {
      console.error('Delete memory error:', error)
      alert('메모리 삭제에 실패했습니다.')
    }
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('ko-KR')
  }

  // 프로필 아이콘 SVG
  const ProfileIcon = () => (
    <svg width="80" height="80" viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="40" cy="40" r="40" fill="#e3e6ee"/>
      <circle cx="40" cy="32" r="14" fill="#b8b6ff"/>
      <ellipse cx="40" cy="60" rx="22" ry="14" fill="#b8b6ff"/>
    </svg>
  )

  if (!user) {
    return <div>로그인이 필요합니다.</div>
  }

  return (
    <div className="mypage-container">
      {/* 프로필 카드 */}
      <div className="mypage-profile-card-modern card-glass">
        <div className="mypage-profile-avatar">
          {user.profileImageUrl ? (
            <img src={user.profileImageUrl} alt={user.username} />
          ) : (
            <ProfileIcon />
          )}
        </div>
        
        <div className="mypage-profile-info">
          {isEditing ? (
            <form onSubmit={handleEditSubmit} className="mypage-edit-form">
              <div className="mypage-edit-group">
                <label>사용자명</label>
                <input
                  type="text"
                  value={editForm.username}
                  onChange={(e) => setEditForm(prev => ({ ...prev, username: e.target.value }))}
                  className="input-glass"
                />
              </div>
              <div className="mypage-edit-group">
                <label>이메일</label>
                <input
                  type="email"
                  value={editForm.email}
                  disabled
                  className="input-glass"
                />
              </div>
              <div className="mypage-edit-actions">
                <button type="submit" className="btn-main">저장</button>
                <button type="button" onClick={handleEditCancel} className="btn-main">취소</button>
              </div>
            </form>
          ) : (
            <>
              <div className="mypage-nickname-row">
                <h2 className="mypage-nickname">{user.username}</h2>
                <button onClick={() => setIsEditing(true)} className="btn-main">
                  <FiEdit3 size={16} /> 편집
                </button>
              </div>
              <div className="mypage-stats">
                <span>메모리 {stats.totalMemories}개</span>
                <span>좋아요 {stats.totalLikes}개</span>
                <span>북마크 {stats.totalBookmarks}개</span>
              </div>
              <p className="mypage-bio">
                오늘도 특별한 순간을 기록하고 있어요 ✨
              </p>
            </>
          )}
        </div>

        <div className="mypage-profile-actions">
          <button className="mypage-action-btn" onClick={() => navigate('/settings')}>
            <FiSettings size={20} />
          </button>
          <button className="mypage-action-btn logout" onClick={handleLogout}>
            <FiLogOut size={20} />
          </button>
        </div>
      </div>

      {/* 통계 카드 */}
      <div className="mypage-stats-modern">
        <div className="mypage-stat-item card-glass">
          <FiImage size={24} />
          <div className="mypage-stat-content">
            <div className="mypage-stat-number">{stats.totalMemories}</div>
            <div className="mypage-stat-label">메모리</div>
          </div>
        </div>
        <div className="mypage-stat-item card-glass">
          <FiHeart size={24} />
          <div className="mypage-stat-content">
            <div className="mypage-stat-number">{stats.totalLikes}</div>
            <div className="mypage-stat-label">받은 좋아요</div>
          </div>
        </div>
        <div className="mypage-stat-item card-glass">
          <FiBookmark size={24} />
          <div className="mypage-stat-content">
            <div className="mypage-stat-number">{stats.totalBookmarks}</div>
            <div className="mypage-stat-label">북마크</div>
          </div>
        </div>
      </div>

      {/* 탭 네비게이션 */}
      <div className="mypage-tabs">
        <button
          className={`mypage-tab btn-main ${activeTab === 'memories' ? 'active' : ''}`}
          onClick={() => setActiveTab('memories')}
        >
          <FiImage size={18} />
          내 메모리
        </button>
        <button
          className={`mypage-tab btn-main ${activeTab === 'bookmarks' ? 'active' : ''}`}
          onClick={() => setActiveTab('bookmarks')}
        >
          <FiBookmark size={18} />
          북마크
        </button>
      </div>

      {/* 탭 콘텐츠 */}
      <div className="mypage-tab-content">
        {isLoading ? (
          <div className="mypage-loading">로딩 중...</div>
        ) : (
          <>
            {activeTab === 'memories' && (
              <div className="mypage-memories">
                {memories.length > 0 ? (
                  memories.map((memory) => (
                    <div key={memory.id} className="mypage-memory-item card-glass">
                      <div className="mypage-memory-content" onClick={() => handleMemoryClick(memory.id)}>
                        <p>{memory.content}</p>
                        {memory.imageUrl && (
                          <div className="mypage-memory-image">
                            <img src={memory.imageUrl} alt="메모리 이미지" />
                          </div>
                        )}
                        <div className="mypage-memory-meta">
                          <span className="badge-pastel">{formatDate(memory.createdAt)}</span>
                          <span className="badge-pastel">❤️ {memory.likeCount}</span>
                          <span className="badge-pastel">🔖 {memory.bookmarkCount}</span>
                        </div>
                      </div>
                      <button
                        className="mypage-memory-delete btn-main"
                        onClick={() => handleDeleteMemory(memory.id)}
                      >
                        삭제
                      </button>
                    </div>
                  ))
                ) : (
                  <div className="mypage-empty card-glass">
                    <FiImage size={48} />
                    <p>아직 작성한 메모리가 없습니다.</p>
                    <button onClick={() => navigate('/write')} className="btn-main">
                      첫 메모리 작성하기
                    </button>
                  </div>
                )}
              </div>
            )}

            {activeTab === 'bookmarks' && (
              <div className="mypage-bookmarks">
                {bookmarks.length > 0 ? (
                  bookmarks.map((memory) => (
                    <div key={memory.id} className="mypage-memory-item card-glass">
                      <div className="mypage-memory-content" onClick={() => handleMemoryClick(memory.id)}>
                        <div className="mypage-memory-author badge-pastel">{memory.username}</div>
                        <p>{memory.content}</p>
                        {memory.imageUrl && (
                          <div className="mypage-memory-image">
                            <img src={memory.imageUrl} alt="메모리 이미지" />
                          </div>
                        )}
                        <div className="mypage-memory-meta">
                          <span className="badge-pastel">{formatDate(memory.createdAt)}</span>
                          <span className="badge-pastel">❤️ {memory.likeCount}</span>
                          <span className="badge-pastel">🔖 {memory.bookmarkCount}</span>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <div className="mypage-empty card-glass">
                    <FiBookmark size={48} />
                    <p>아직 북마크한 메모리가 없습니다.</p>
                    <button onClick={() => navigate('/')} className="btn-main">
                      메모리 둘러보기
                    </button>
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
} 