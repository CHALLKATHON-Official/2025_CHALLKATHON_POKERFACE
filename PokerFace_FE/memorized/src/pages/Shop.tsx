import './Shop.css'

export default function Shop() {
  return (
    <div className="card-glass shop-card fade-in">
      <div className="shop-title">
        <span>상점</span>
        <span className="shop-icon">🪙✨</span>
      </div>
      <div className="shop-coins">보유 코인: <b>120</b></div>
      <div className="shop-items">
        <div className="shop-item">
          <span className="shop-item-name">별가루 배경</span>
          <button className="shop-buy-btn">구매</button>
        </div>
        <div className="shop-item">
          <span className="shop-item-name">시계 아이템</span>
          <button className="shop-buy-btn">구매</button>
        </div>
      </div>
    </div>
  )
} 