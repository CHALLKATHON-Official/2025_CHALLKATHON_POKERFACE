import './Write.css'
import { useRef } from 'react'

export default function Write() {
  const textareaRef = useRef<HTMLTextAreaElement>(null)

  const handleResizeHeight = () => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto' 
      textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px` 
    }
  }

  return (
    <div className="card-glass write-card fade-in">
      <div className="write-title">
        <span>기억 작성하기</span>
      </div>
      <form className="write-form">
        <input className="write-input" type="text" placeholder="제목을 입력하세요" />
        <textarea
          className="write-textarea"
          placeholder="당신의 소중한 기억을 기록해보세요."
          rows={1} // 처음 높이 최소화
          ref={textareaRef}
          onInput={handleResizeHeight}
        />
        <label className="write-upload">
          <input type="file" style={{ display: 'none' }} />
          <span>이미지 업로드</span>
        </label>
        <button className="write-btn" type="submit">기억 저장</button>
      </form>
    </div>
  )
}
