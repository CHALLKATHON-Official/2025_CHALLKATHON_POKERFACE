import { useState } from 'react'
import Feed from './Feed'
import MyRoom from './MyRoom'
import Library from './Library'
import Shop from './Shop'
import './MyPage.css'

const TABS = [
  { key: 'feed', label: '내 피드' },
  { key: 'myroom', label: '미니홈피' },
  { key: 'library', label: '기억의 서고' },
  { key: 'shop', label: '상점' },
]

export default function MyPage() {
  const [tab, setTab] = useState('feed')
  return (
    <div className="card-glass mypage-card fade-in">
      <div className="mypage-profile">
        <img className="mypage-avatar" src="https://i.pravatar.cc/100?img=3" alt="프로필" />
        <div className="mypage-nickname">앨리스</div>
        <span className="mypage-icon">🌙✨</span>
      </div>
      <div className="mypage-tabs">
        {TABS.map(t => (
          <button
            key={t.key}
            className={tab === t.key ? 'mypage-tab active' : 'mypage-tab'}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>
      <div className="mypage-tab-content">
        {tab === 'feed' && <Feed />}
        {tab === 'myroom' && <MyRoom />}
        {tab === 'library' && <Library />}
        {tab === 'shop' && <Shop />}
      </div>
    </div>
  )
} 