import { useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiImage, FiX } from 'react-icons/fi'
import { memoryAPI } from '../api/memory'
import './Write.css'

export default function Write() {
  const navigate = useNavigate()
  const [content, setContent] = useState('')
  const [emotion, setEmotion] = useState('')
  const [imageFile, setImageFile] = useState<File | null>(null)
  const [imagePreview, setImagePreview] = useState<string>('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!content.trim()) {
      setError('내용을 입력해주세요.')
      return
    }

    setIsLoading(true)
    setError('')

    try {
      const response = await memoryAPI.createMemory({
        content: content.trim(),
        emotion: emotion || undefined,
        image: imageFile || undefined
      })
      
      if (response.success) {
        navigate('/')
      } else {
        setError('메모리 작성에 실패했습니다.')
      }
    } catch (err: any) {
      console.error('Write memory error:', err)
      setError('메모리 작성 중 오류가 발생했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      setImageFile(file)
      const reader = new FileReader()
      reader.onload = (e) => {
        setImagePreview(e.target?.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const removeImage = () => {
    setImageFile(null)
    setImagePreview('')
    if (fileInputRef.current) {
      fileInputRef.current.value = ''
    }
  }

  const handleImageClick = () => {
    fileInputRef.current?.click()
  }

  return (
    <div className="write-container">
      <div className="write-header">
        <button 
          className="write-cancel-btn" 
          onClick={() => navigate('/')}
          disabled={isLoading}
        >
          취소
        </button>
        <h1>새 메모리</h1>
        <button 
          className="write-publish-btn"
          onClick={handleSubmit}
          disabled={isLoading || !content.trim()}
        >
          {isLoading ? '게시 중...' : '게시'}
        </button>
      </div>

      <form onSubmit={handleSubmit} className="write-form">
        <div className="write-content">
          <textarea
            className="write-textarea"
            placeholder="오늘의 기억을 기록해보세요..."
            value={content}
            onChange={(e) => {
              setContent(e.target.value)
              if (error) setError('')
            }}
            disabled={isLoading}
            maxLength={1000}
          />
          
          {imagePreview && (
            <div className="write-image-preview">
              <img src={imagePreview} alt="Preview" />
              <button 
                type="button" 
                className="write-image-remove"
                onClick={removeImage}
                disabled={isLoading}
              >
                <FiX size={20} />
              </button>
            </div>
          )}
        </div>

        {error && (
          <div className="write-error">
            {error}
          </div>
        )}

        <div className="write-actions">
          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            onChange={handleImageUpload}
            style={{ display: 'none' }}
            disabled={isLoading}
          />
          <button 
            type="button"
            className="write-image-btn"
            onClick={handleImageClick}
            disabled={isLoading}
          >
            <FiImage size={20} />
            이미지 추가
          </button>
        </div>
      </form>
    </div>
  )
} 