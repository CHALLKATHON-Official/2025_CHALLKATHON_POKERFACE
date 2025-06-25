import './Library.css'

export default function Library() {
  return (
    <div className="card-glass library-card fade-in">
      <div className="library-title">
        <span>기억의 서고</span>
        <span className="library-icon">📚✨</span>
      </div>
      <form className="library-form">
        <input className="library-input" type="text" placeholder="책 제목, 저자 등으로 검색" />
        <button className="library-btn" type="submit">책 검색</button>
      </form>
      <div className="library-section-title">내가 등록한 책</div>
      <ul className="library-list">
        <li>이상한 나라의 앨리스</li>
        <li>시간의 연금술</li>
      </ul>
    </div>
  )
} 