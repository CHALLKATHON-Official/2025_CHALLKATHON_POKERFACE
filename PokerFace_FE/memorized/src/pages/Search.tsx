import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { userAPI } from '../api/user'
import type { UserSearchResponse } from '../api/user'
import './Search.css'

export default function Search() {
  const navigate = useNavigate()
  const [query, setQuery] = useState('')
  const [users, setUsers] = useState<UserSearchResponse[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  const handleSearch = async (searchQuery: string) => {
    if (!searchQuery.trim()) {
      setUsers([])
      return
    }

    setIsLoading(true)
    setError('')

    try {
      const response = await userAPI.searchUsers(searchQuery.trim())
      if (response.success) {
        setUsers(response.data)
      } else {
        setError('검색에 실패했습니다.')
      }
    } catch (err: any) {
      console.error('Search error:', err)
      setError('검색 중 오류가 발생했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    setQuery(value)
    
    // 디바운스 처리
    const timeoutId = setTimeout(() => {
      handleSearch(value)
    }, 300)

    return () => clearTimeout(timeoutId)
  }

  const handleUserClick = (userId: number) => {
    navigate(`/profile/${userId}`)
  }

  return (
    <div className="search-container">
      <div className="search-header">
        <h2>사용자 검색</h2>
      </div>
      
      <div className="search-input-container">
        <input
          type="text"
          placeholder="사용자명 또는 이메일로 검색..."
          value={query}
          onChange={handleInputChange}
          className="search-input"
        />
      </div>

      {error && (
        <div className="search-error">
          {error}
        </div>
      )}

      <div className="search-results">
        {isLoading ? (
          <div className="search-loading">검색 중...</div>
        ) : users.length > 0 ? (
          users.map((user) => (
            <div 
              key={user.id} 
              className="search-user-item"
              onClick={() => handleUserClick(user.id)}
            >
              <img 
                src={user.profileImageUrl || `https://i.pravatar.cc/40?img=${user.id}`} 
                alt={user.username}
                className="search-user-avatar"
              />
              <div className="search-user-info">
                <div className="search-username">{user.username}</div>
                <div className="search-email">{user.email}</div>
              </div>
            </div>
          ))
        ) : query.trim() ? (
          <div className="search-no-results">
            검색 결과가 없습니다.
          </div>
        ) : null}
      </div>
    </div>
  )
} 