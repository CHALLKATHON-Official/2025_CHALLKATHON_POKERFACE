import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom'
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Feed from './pages/Feed'

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
        </Routes>
      </div>
    </Router>
  )
}

export default App
