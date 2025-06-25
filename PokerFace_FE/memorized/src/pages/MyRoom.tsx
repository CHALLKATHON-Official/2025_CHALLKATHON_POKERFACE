import './MyRoom.css'

export default function MyRoom() {
  return (
    <div className="card-glass myroom-card fade-in">
      <div className="myroom-title">
        <span>나만의 마이룸</span>
        <span className="myroom-icon">🗝️🚪</span>
      </div>
      <div className="myroom-preview">
        <div className="myroom-bg">배경</div>
        <div className="myroom-items">아이템 배치 예시</div>
        <div className="myroom-music">🎵 음악: Wonderland</div>
      </div>
      <button className="myroom-btn">마이룸 저장</button>
    </div>
  )
} 