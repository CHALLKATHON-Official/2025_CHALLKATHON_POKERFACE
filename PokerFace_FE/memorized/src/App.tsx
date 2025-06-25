import { BrowserRouter as Router, Routes, Route, Link, useLocation, useNavigate } from 'react-router-dom'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Feed from './pages/Feed'
import Write from './pages/Write'
import Search from './pages/Search'
import MyPage from './pages/MyPage'
import './App.css'

function MenuBar() {
  const location = useLocation()
  const navigate = useNavigate()
  return (
    <nav className="menubar">
      <div className="menubar-inner">
        <Link to="/" className="menubar-logo">Memorized</Link>
        <div className="menubar-actions">
          <button className={location.pathname === '/' ? 'menubar-btn active' : 'menubar-btn'} onClick={() => navigate('/')}>홈</button>
          <button className={location.pathname === '/search' ? 'menubar-btn active' : 'menubar-btn'} onClick={() => navigate('/search')}>검색</button>
          <button className={location.pathname === '/write' ? 'menubar-btn active' : 'menubar-btn'} onClick={() => navigate('/write')}>글쓰기</button>
          <button className={location.pathname === '/notifications' ? 'menubar-btn active' : 'menubar-btn'}>알림</button>
          <button className={location.pathname === '/mypage' ? 'menubar-btn active' : 'menubar-btn'} onClick={() => navigate('/mypage')}>프로필</button>
        </div>
      </div>
    </nav>
  )
}

function App() {
  return (
    <Router>
      <MenuBar />
      <div className="main-content">
        <Routes>
          <Route path="/" element={<Feed />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/write" element={<Write />} />
          <Route path="/search" element={<Search />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
