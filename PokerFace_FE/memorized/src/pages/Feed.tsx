import './Feed.css'

const sampleMemories = [
  { id: 1, user: 'alice', content: '오늘은 이상한 나라에서 토끼를 만났어요.', date: '2024-06-25' },
  { id: 2, user: 'queen', content: '시간이 멈춘 듯한 오후, 차 한잔.', date: '2024-06-24' },
]

export default function Feed() {
  return (
    <div className="feed-root">
      <nav className="feed-nav">
        <span className="feed-logo">Memorized</span>
        <button className="feed-logout">로그아웃</button>
      </nav>
      <div className="feed-container">
        <h2 className="alice-title">기억 피드</h2>
        <div className="feed-list">
          {sampleMemories.map(m => (
            <div className="feed-card" key={m.id}>
              <div className="feed-card-user">{m.user}</div>
              <div className="feed-card-content">{m.content}</div>
              <div className="feed-card-date">{m.date}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
} 