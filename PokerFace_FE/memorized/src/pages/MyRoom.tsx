import { useState } from 'react'
import './MyRoom.css'

export default function MyRoom() {
  const [visitorCount] = useState(1234)
  const [isFriend] = useState(false)
  const [guestbook] = useState([
    { id: 1, author: '친구1', message: '방문했어요!', date: '2025-01-15' },
    { id: 2, author: '친구2', message: '오늘도 좋은 하루 되세요~', date: '2025-01-14' },
    { id: 3, author: '친구3', message: '새로운 추억이 가득하네요', date: '2025-01-13' },
  ])

  return (
    <div className="myroom-container">
      {/* 배경 이미지 */}
      <div className="myroom-background">
        <img src="https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800" alt="배경" />
      </div>
      
      {/* 메인 콘텐츠 */}
      <div className="myroom-content">
        {/* 프로필 섹션 */}
        <div className="myroom-profile">
          <div className="myroom-profile-image">
            <img src="https://i.pravatar.cc/100?img=3" alt="프로필" />
          </div>
          <div className="myroom-profile-info">
            <h2>앨리스의 미니홈피</h2>
            <p>시간을 수집하는 사람</p>
            <div className="myroom-stats">
              <span>방문자: {visitorCount}명</span>
              <span>일촌: {isFriend ? '맺음' : '안맺음'}</span>
            </div>
            <button className="myroom-friend-btn">
              {isFriend ? '일촌 끊기' : '일촌 맺기'}
            </button>
          </div>
        </div>

        {/* 메뉴 섹션 */}
        <div className="myroom-menu">
          <div className="myroom-menu-item">
            <div className="menu-icon">📝</div>
            <span>방명록</span>
            <span className="menu-count">{guestbook.length}</span>
          </div>
          <div className="myroom-menu-item">
            <div className="menu-icon">📷</div>
            <span>사진첩</span>
            <span className="menu-count">24</span>
          </div>
          <div className="myroom-menu-item">
            <div className="menu-icon">🎵</div>
            <span>음악</span>
            <span className="menu-count">3</span>
          </div>
          <div className="myroom-menu-item">
            <div className="menu-icon">📖</div>
            <span>다이어리</span>
            <span className="menu-count">12</span>
          </div>
        </div>

        {/* 방명록 섹션 */}
        <div className="myroom-guestbook">
          <h3>방명록</h3>
          <div className="guestbook-list">
            {guestbook.map(guest => (
              <div key={guest.id} className="guestbook-item">
                <div className="guest-info">
                  <span className="guest-author">{guest.author}</span>
                  <span className="guest-date">{guest.date}</span>
                </div>
                <div className="guest-message">{guest.message}</div>
              </div>
            ))}
          </div>
          <div className="guestbook-write">
            <input type="text" placeholder="방명록을 남겨주세요..." />
            <button>작성</button>
          </div>
        </div>

        {/* 최근 사진 섹션 */}
        <div className="myroom-recent-photos">
          <h3>최근 사진</h3>
          <div className="photo-grid">
            <img src="https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=200" alt="사진1" />
            <img src="https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=200" alt="사진2" />
            <img src="https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=200" alt="사진3" />
            <img src="https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=200" alt="사진4" />
          </div>
        </div>
      </div>
    </div>
  )
} 