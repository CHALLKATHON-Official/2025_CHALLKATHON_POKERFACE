import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom'
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Feed from './pages/Feed'
import Write from './pages/Write'
import Search from './pages/Search'
import MyPage from './pages/MyPage'
import MyRoom from './pages/MyRoom'
import Shop from './pages/Shop'
import Library from './pages/Library'

function Navbar() {
  const location = useLocation()
  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="navbar-logo">Memorized</Link>
        <div className="navbar-actions">
          {location.pathname === '/login' ? (
            <Link to="/signup" className="navbar-btn">회원가입</Link>
          ) : location.pathname === '/signup' ? (
            <Link to="/login" className="navbar-btn">로그인</Link>
          ) : (
            <>
              <Link to="/login" className="navbar-btn">로그인</Link>
              <Link to="/signup" className="navbar-btn">회원가입</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}

function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
      <Navbar />
      <div className="main-content">
        <Routes>
          <Route path="/" element={<Feed />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/feed" element={<Feed />} />
          <Route path="/write" element={<Write />} />
          <Route path="/search" element={<Search />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/myroom" element={<MyRoom />} />
          <Route path="/shop" element={<Shop />} />
          <Route path="/library" element={<Library />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
