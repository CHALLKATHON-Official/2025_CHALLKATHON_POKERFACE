import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import Layout from './components/Layout'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Feed from './pages/Feed'
import Write from './pages/Write'
import Search from './pages/Search'
import MyPage from './pages/MyPage'
import Shop from './pages/Shop'
import Library from './pages/Library'
import Notification from './pages/Notification'
import './App.css'

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/" element={<Layout />}>
              <Route index element={<Feed />} />
              <Route path="write" element={<Write />} />
              <Route path="search" element={<Search />} />
              <Route path="mypage" element={<MyPage />} />
              <Route path="shop" element={<Shop />} />
              <Route path="library" element={<Library />} />
              <Route path="notification" element={<Notification />} />
            </Route>
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  )
}

export default App
