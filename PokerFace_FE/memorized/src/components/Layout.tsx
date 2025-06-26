import { useState, useEffect } from 'react'
import { Outlet, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { FiHome, FiSearch, FiPlusSquare, FiBell, FiUser, FiX } from 'react-icons/fi'
import Search from '../pages/Search'
import Notification from '../pages/Notification'
import './Layout.css'

function Modal({ open, onClose, children }: { open: boolean, onClose: () => void, children: React.ReactNode }) {
  if (!open) return null
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <button className="modal-close" onClick={onClose}><FiX size={22} /></button>
        {children}
      </div>
    </div>
  )
}

function SideBar({ onSearch, onNotifications }: { onSearch: () => void, onNotifications: () => void }) {
  const location = useLocation()
  const navigate = useNavigate()
  const { user } = useAuth()
  
  return (
    <aside className="sidebar card-glass">
      <button 
        className={location.pathname === '/' ? 'sidebar-btn btn-main active' : 'sidebar-btn btn-main'} 
        onClick={() => navigate('/')}
      > 
        <FiHome size={24} /> 
      </button>
      <button className={'sidebar-btn btn-main'} onClick={onSearch}> 
        <FiSearch size={24} /> 
      </button>
      <button 
        className={location.pathname === '/write' ? 'sidebar-btn btn-main active' : 'sidebar-btn btn-main'} 
        onClick={() => navigate('/write')}
      > 
        <FiPlusSquare size={24} /> 
      </button>
      <button className={'sidebar-btn btn-main'} onClick={onNotifications}> 
        <FiBell size={24} /> 
      </button>
      <button 
        className={location.pathname === '/mypage' ? 'sidebar-btn btn-main active' : 'sidebar-btn btn-main'} 
        onClick={() => navigate('/mypage')}
      > 
        <FiUser size={24} /> 
      </button>
    </aside>
  )
}

function MainLayout({ children, hideRight = false }: { children: React.ReactNode, hideRight?: boolean }) {
  const [searchOpen, setSearchOpen] = useState(false)
  const [notiOpen, setNotiOpen] = useState(false)
  const { user } = useAuth()
  
  return (
    <div className="main-layout">
      <SideBar onSearch={() => setSearchOpen(true)} onNotifications={() => setNotiOpen(true)} />
      <main className="main-center">{children}</main>
      {!hideRight && (
        <aside className="main-right card-glass">
          <div className="right-profile-card card-glass">
            <div className="right-profile-avatar">
              <img 
                src={`https://i.pravatar.cc/40?img=1`}
                alt={user?.username || '사용자'}
              />
              <button className="profile-img-edit-btn btn-main" style={{marginTop: 6, fontSize: 12, background: 'none', border: 'none', color: '#888', cursor: 'pointer'}}>
                이미지 변경
              </button>
            </div>
            <div className="right-profile-info">
              <div className="right-profile-nick badge-pastel">{user?.username || '사용자'}</div>
              <div className="right-profile-id">{user?.email || ''}</div>
            </div>
          </div>
          <div className="right-suggest-title">회원님을 위한 추천</div>
          <div className="right-suggest-list">
            <div className="right-suggest-item card-glass">
              <span className="badge-pastel">jamjamjam_1122</span>
              <button className="btn-main">팔로우</button>
            </div>
            <div className="right-suggest-item card-glass">
              <span className="badge-pastel">wacko.y</span>
              <button className="btn-main">팔로우</button>
            </div>
          </div>
        </aside>
      )}
      <Modal open={searchOpen} onClose={() => setSearchOpen(false)}>
        <Search />
      </Modal>
      <Modal open={notiOpen} onClose={() => setNotiOpen(false)}>
        <Notification />
      </Modal>
    </div>
  )
}

export default function Layout() {
  const location = useLocation()
  const navigate = useNavigate()
  const { isAuthenticated, isLoading } = useAuth()
  const hideRight = location.pathname === '/write' || location.pathname === '/mypage'
  
  // 인증 상태 체크
  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      navigate('/main')
    }
  }, [isAuthenticated, isLoading, navigate])
  
  // 로딩 중이거나 인증되지 않은 경우 로딩 표시
  if (isLoading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>로딩 중...</p>
      </div>
    )
  }
  
  if (!isAuthenticated) {
    return null // 리다이렉트 중
  }
  
  return (
    <MainLayout hideRight={hideRight}>
      <Outlet />
    </MainLayout>
  )
} 