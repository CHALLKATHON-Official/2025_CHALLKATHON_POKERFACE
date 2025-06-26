import { useState } from 'react'
import MyRoom from './MyRoom'
import Library from './Library'
import './MyPage.css'

const TABS = [
  { key: 'myroom', label: '미니홈피' },
  { key: 'library', label: '기억의 서고' },
]

export default function MyPage() {
  const [tab, setTab] = useState('myroom')
  return (
    <div className="mypage-root">
      <div className="mypage-profile-top">
        <img className="mypage-avatar" src="https://i.pravatar.cc/120?img=3" alt="프로필" />
        <div className="mypage-profile-info">
          <div className="mypage-nickname-row">
            <span className="mypage-nickname">앨리스</span>
            <button className="mypage-edit-btn">프로필 편집</button>
          </div>
          <div className="mypage-stats">
            <span><b>12</b> 게시물</span>
            <span><b>390</b> 팔로워</span>
            <span><b>439</b> 팔로잉</span>
          </div>
          <div className="mypage-bio">시간을 수집하는 사람<br /><a href="https://alice.com" target="_blank" rel="noreferrer">alice.com</a></div>
        </div>
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
        {tab === 'myroom' && <MyRoom />}
        {tab === 'library' && <Library />}
      </div>
    </div>
  )
} 