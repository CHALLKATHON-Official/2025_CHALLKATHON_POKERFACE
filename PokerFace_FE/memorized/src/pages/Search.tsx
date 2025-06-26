import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiSearch, FiClock, FiX, FiUser } from 'react-icons/fi'
import { userAPI } from '../api/user'
import type { UserSearchResponse, UserSearchHistoryResponse } from '../api/user'
import './Search.css'

export default function Search() {
  const navigate = useNavigate()
  const [query, setQuery] = useState('')
  const [users, setUsers] = useState<UserSearchResponse[]>([])
  const [searchHistory, setSearchHistory] = useState<UserSearchHistoryResponse[]>([])
  const [suggestions, setSuggestions] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const [showSuggestions, setShowSuggestions] = useState(false)
  const searchTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  // 검색 히스토리 로드
  useEffect(() => {
    loadSearchHistory()
  }, [])

  const loadSearchHistory = async () => {
    try {
      const response = await userAPI.getSearchHistory()
      if (response.success) {
        setSearchHistory(response.data)
      }
    } catch (error) {
      console.error('Load search history error:', error)
    }
  }

  const handleSearch = async (searchQuery: string) => {
    if (!searchQuery.trim()) {
      setUsers([])
      setSuggestions([])
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
    setShowSuggestions(true)
    
    // 기존 타임아웃 클리어
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current)
    }

    // 디바운스 처리
    searchTimeoutRef.current = setTimeout(() => {
      if (value.trim()) {
        handleSearch(value)
        // 키워드 제안도 함께 요청
        handleSuggestions(value)
      } else {
        setUsers([])
        setSuggestions([])
      }
    }, 300)
  }

  const handleSuggestions = async (prefix: string) => {
    try {
      const response = await userAPI.suggestKeywords(prefix)
      if (response.success) {
        setSuggestions(response.data.suggestions || [])
      }
    } catch (error) {
      console.error('Suggestions error:', error)
    }
  }

  const handleUserClick = (userId: number) => {
    navigate(`/profile/${userId}`)
  }

  const handleHistoryClick = (historyQuery: string) => {
    setQuery(historyQuery)
    handleSearch(historyQuery)
    setShowSuggestions(false)
  }

  const handleSuggestionClick = (suggestion: string) => {
    setQuery(suggestion)
    handleSearch(suggestion)
    setShowSuggestions(false)
  }

  const handleDeleteHistory = async (historyId: number) => {
    try {
      await userAPI.deleteSearchHistory(historyId)
      setSearchHistory(prev => prev.filter(h => h.id !== historyId))
    } catch (error) {
      console.error('Delete history error:', error)
    }
  }

  const handleClearHistory = async () => {
    try {
      // 모든 히스토리 삭제
      await Promise.all(searchHistory.map(h => userAPI.deleteSearchHistory(h.id)))
      setSearchHistory([])
    } catch (error) {
      console.error('Clear history error:', error)
    }
  }

  // 프로필 아이콘 SVG
  const ProfileIcon = () => (
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="20" cy="20" r="20" fill="#e3e6ee"/>
      <circle cx="20" cy="16" r="7" fill="#b8b6ff"/>
      <ellipse cx="20" cy="30" rx="11" ry="7" fill="#b8b6ff"/>
    </svg>
  )

  return (
    <div className="search-container card-glass">
      <div className="search-header">
        <h1>사용자 검색</h1>
        <p>친구나 관심 있는 사용자를 찾아보세요</p>
      </div>

      <div className="search-input-group">
        <div className="search-input-wrapper">
          <FiSearch className="search-icon" size={20} />
          <input
            type="text"
            className="search-input input-glass"
            placeholder="사용자명을 입력하세요"
            value={query}
            onChange={handleInputChange}
            onFocus={() => setShowSuggestions(true)}
          />
          {query && (
            <button
              className="search-clear-btn btn-main"
              onClick={() => {
                setQuery('')
                setUsers([])
                setSuggestions([])
              }}
            >
              <FiX size={16} />
            </button>
          )}
        </div>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {/* 검색 결과 */}
      {users.length > 0 && (
        <div className="search-results">
          <h3>검색 결과 ({users.length})</h3>
          {users.map((user) => (
            <div
              key={user.id}
              className="search-user-item card-glass"
              onClick={() => handleUserClick(user.id)}
            >
              <div className="search-user-avatar">
                {user.profileImageUrl ? (
                  <img src={user.profileImageUrl} alt={user.username} />
                ) : (
                  <ProfileIcon />
                )}
              </div>
              <div className="search-user-info">
                <div className="search-user-name badge-pastel">{user.username}</div>
                <div className="search-user-email">{user.email}</div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* 검색 제안 */}
      {showSuggestions && suggestions.length > 0 && !query && (
        <div className="search-suggestions">
          <h3>추천 검색어</h3>
          {suggestions.map((suggestion, index) => (
            <button
              key={index}
              className="search-suggestion-item btn-main"
              onClick={() => handleSuggestionClick(suggestion)}
            >
              <FiSearch size={16} />
              <span>{suggestion}</span>
            </button>
          ))}
        </div>
      )}

      {/* 검색 히스토리 */}
      {showSuggestions && searchHistory.length > 0 && !query && !suggestions.length && (
        <div className="search-history">
          <div className="search-history-header">
            <h3>최근 검색</h3>
            <button
              className="search-history-clear btn-main"
              onClick={handleClearHistory}
            >
              전체 삭제
            </button>
          </div>
          {searchHistory.map((history) => (
            <div key={history.id} className="search-history-item card-glass">
              <button
                className="search-history-content"
                onClick={() => handleHistoryClick(history.query)}
              >
                <FiClock size={16} />
                <span className="badge-pastel">{history.query}</span>
              </button>
              <button
                className="search-history-delete btn-main"
                onClick={() => handleDeleteHistory(history.id)}
              >
                <FiX size={16} />
              </button>
            </div>
          ))}
        </div>
      )}

      {/* 로딩 상태 */}
      {isLoading && (
        <div className="search-loading">
          검색 중...
        </div>
      )}

      {/* 검색 결과가 없을 때 */}
      {!isLoading && query && users.length === 0 && !error && (
        <div className="search-no-results card-glass">
          <FiUser size={48} />
          <p>검색 결과가 없습니다.</p>
          <p>다른 키워드로 검색해보세요.</p>
        </div>
      )}
    </div>
  )
} 