import './Write.css'

export default function Write() {
  return (
    <div className="card-glass write-card fade-in">
      <div className="write-title">
        <span>기억 작성하기</span>
        <span className="write-icon">⏰✨</span>
      </div>
      <form className="write-form">
        <input className="write-input" type="text" placeholder="제목을 입력하세요" />
        <textarea className="write-textarea" placeholder="당신의 소중한 기억을 기록해보세요." rows={5} />
        <label className="write-upload">
          <input type="file" style={{ display: 'none' }} />
          <span>이미지 업로드</span>
        </label>
        <button className="write-btn" type="submit">기억 저장</button>
      </form>
    </div>
  )
} 