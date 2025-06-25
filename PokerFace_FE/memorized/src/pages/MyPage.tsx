import './MyPage.css'

export default function MyPage() {
  return (
    <div className="card-glass mypage-card fade-in">
      <div className="mypage-profile">
        <img className="mypage-avatar" src="https://i.pravatar.cc/100?img=3" alt="프로필" />
        <div className="mypage-nickname">앨리스</div>
        <span className="mypage-icon">🌙✨</span>
      </div>
      <div className="mypage-tabs">
        <button className="mypage-tab active">내 기억</button>
        <button className="mypage-tab">북마크</button>
        <button className="mypage-tab">좋아요</button>
        <button className="mypage-tab">알림</button>
      </div>
      <div className="mypage-list">
        <div className="mypage-list-item">"이상한 나라의 추억"</div>
        <div className="mypage-list-item">"시계토끼와의 만남"</div>
      </div>
    </div>
  )
} 