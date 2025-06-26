import { useState } from 'react'
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
    <aside className="sidebar">
      <button 
        className={location.pathname === '/' ? 'sidebar-btn active' : 'sidebar-btn'} 
        onClick={() => navigate('/')}
      > 
        <FiHome size={24} /> 
      </button>
      <button className={'sidebar-btn'} onClick={onSearch}> 
        <FiSearch size={24} /> 
      </button>
      <button 
        className={location.pathname === '/write' ? 'sidebar-btn active' : 'sidebar-btn'} 
        onClick={() => navigate('/write')}
      > 
        <FiPlusSquare size={24} /> 
      </button>
      <button className={'sidebar-btn'} onClick={onNotifications}> 
        <FiBell size={24} /> 
      </button>
      <button 
        className={location.pathname === '/mypage' ? 'sidebar-btn active' : 'sidebar-btn'} 
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
        <aside className="main-right">
          <div className="right-profile-card">
            <div className="right-profile-avatar">
              <img 
                src={user?.profileImageUrl || `https://i.pravatar.cc/40?img=${user?.id || 1}`} 
                alt={user?.username || '사용자'}
              />
            </div>
            <div className="right-profile-info">
              <div className="right-profile-nick">{user?.username || '앨리스'}</div>
              <div className="right-profile-id">{user?.email || 'alice_01'}</div>
            </div>
          </div>
          <div className="right-suggest-title">회원님을 위한 추천</div>
          <div className="right-suggest-list">
            <div className="right-suggest-item">
              <span>jamjamjam_1122</span>
              <button>팔로우</button>
            </div>
            <div className="right-suggest-item">
              <span>wacko.y</span>
              <button>팔로우</button>
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
  const hideRight = location.pathname === '/write' || location.pathname === '/mypage'
  
  return (
    <MainLayout hideRight={hideRight}>
      <Outlet />
    </MainLayout>
  )
} 