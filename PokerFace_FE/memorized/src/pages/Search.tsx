import './Search.css'

export default function Search() {
  return (
    <div className="card-glass search-card fade-in">
      <div className="search-title">
        <span>기억 검색</span>
      </div>
      <form className="search-form">
        <input className="search-input" type="text" placeholder="키워드로 기억을 찾아보세요" />
        <button className="search-btn" type="submit">검색</button>
      </form>
      <div className="search-section">
        <div className="search-section-title">최근 검색어</div>
        <ul className="search-history-list">
          <li>앨리스 
    <button className='delete-btn'>X</button></li>
          <li>시간 
    <button className='delete-btn'>X</button></li>
        </ul>
      </div>
      <div className="search-section">
        <div className="search-section-title">검색 결과</div>
        <ul className="search-result-list">
          <li>"앨리스와의 만남"</li>
          <li>"이상한 나라의 시계탑"</li>
        </ul>
      </div>
    </div>
  )
} 