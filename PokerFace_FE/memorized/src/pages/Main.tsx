import React from 'react';
import { Link } from 'react-router-dom';
import './Main.css';

const features = [
  { icon: '📝', color: '#ffe6a7', text: '감정 기록' },
  { icon: '💝', color: '#ffd6e0', text: '소통과 공감' },
  { icon: '🔍', color: '#c6f1e7', text: '친구 찾기' },
  { icon: '🎨', color: '#e0d6ff', text: '나만의 공간' },
];

const Main: React.FC = () => {
  return (
    <div className="main-container">
      <div className="main-content">
        <div className="main-card">
          <div className="main-header">
            <span className="main-logo-emoji">✨</span>
            <h1>Memorized</h1>
            <p className="main-subtitle">시간을 수집하는 공간</p>
          </div>
          <div className="main-description">
            <span>
              소중한 순간을 기록하고 공유하는 소셜 메모리 플랫폼입니다.
            </span>
          </div>
          <div className="main-feature-steps main-feature-steps-grid">
            {features.map((f, i) => (
              <div className="feature-step" key={i}>
                <span className="feature-badge" style={{ background: f.color }}>{f.icon}</span>
                <span className="feature-text">{f.text}</span>
              </div>
            ))}
          </div>
          <div className="main-actions">
            <Link to="/signup" className="main-button signup-button">회원가입</Link>
            <div className="or-divider"><span>또는</span></div>
            <Link to="/login" className="main-button login-button">로그인</Link>
          </div>
          <div className="main-footer">
            <p>이미 계정이 있으신가요? <Link to="/login" className="footer-link">로그인하기</Link></p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Main; 