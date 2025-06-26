import React, { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { FiUpload, FiX, FiMapPin, FiSun, FiEye, FiEyeOff, FiSave } from 'react-icons/fi'
import { useAuth } from '../contexts/AuthContext'
import { memoryAPI } from '../api/memory'
import './Write.css'

interface WriteFormData {
  content: string
  imageFile: File | null
  location: string
  weather: string
  isPublic: boolean
}

const WEATHER_OPTIONS = [
  { value: 'SUNNY', label: '☀️ 맑음', icon: '☀️' },
  { value: 'CLOUDY', label: '☁️ 흐림', icon: '☁️' },
  { value: 'RAINY', label: '🌧️ 비', icon: '🌧️' },
  { value: 'SNOWY', label: '❄️ 눈', icon: '❄️' },
  { value: 'WINDY', label: '💨 바람', icon: '💨' },
  { value: 'FOGGY', label: '🌫️ 안개', icon: '🌫️' }
]

export default function Write() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const fileInputRef = useRef<HTMLInputElement>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [showPreview, setShowPreview] = useState(false)
  const [autoSaveStatus, setAutoSaveStatus] = useState('')
  
  const [formData, setFormData] = useState<WriteFormData>({
    content: '',
    imageFile: null,
    location: '',
    weather: '',
    isPublic: true
  })

  const [imagePreview, setImagePreview] = useState<string | null>(null)

  // 자동 저장 기능
  useEffect(() => {
    if (formData.content && !isLoading) {
      const timer = setTimeout(() => {
        localStorage.setItem('write_draft', JSON.stringify(formData))
        setAutoSaveStatus('자동 저장됨')
        setTimeout(() => setAutoSaveStatus(''), 2000)
      }, 2000)
      
      return () => clearTimeout(timer)
    }
  }, [formData, isLoading])

  // 임시 저장된 데이터 불러오기
  useEffect(() => {
    const savedDraft = localStorage.getItem('write_draft')
    if (savedDraft) {
      const draft = JSON.parse(savedDraft)
      setFormData(prev => ({ ...prev, ...draft, imageFile: null, imagePreview: null }))
    }
  }, [])

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setFormData(prev => ({ ...prev, content: e.target.value }))
  }

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      if (file.size > 5 * 1024 * 1024) { // 5MB 제한
        alert('이미지 크기는 5MB 이하여야 합니다.')
        return
      }
      
      setFormData(prev => ({ ...prev, imageFile: file }))
      
      const reader = new FileReader()
      reader.onload = (e) => {
        setImagePreview(e.target?.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const removeImage = () => {
    setFormData(prev => ({ ...prev, imageFile: null }))
    setImagePreview(null)
    if (fileInputRef.current) {
      fileInputRef.current.value = ''
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    console.log('submit!')
    e.preventDefault()
    
    if (!user) {
      alert('로그인 후 이용해주세요.')
      return
    }
    if (!formData.content.trim()) {
      alert('내용을 입력해주세요.')
      return
    }

    setIsLoading(true)
    
    try {
      const res = await memoryAPI.createMemory({
        content: formData.content,
        image: formData.imageFile || undefined
      })
      // ApiResponse 래핑이 아니고 바로 MemoryResponse가 올 경우도 처리
      const memoryId = (res as any)?.data?.id || (res as any)?.id
      if (!memoryId) {
        throw new Error('메모리 생성 응답이 올바르지 않습니다.')
      }
      // 임시 저장 데이터 삭제
      localStorage.removeItem('write_draft')
      navigate(`/memory/${memoryId}`)
    } catch (error: any) {
      console.error('메모리 작성 실패:', error)
      if (error?.response?.data?.message) {
        alert(error.response.data.message)
      } else {
        alert('메모리 작성에 실패했습니다.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  const handleSaveDraft = () => {
    localStorage.setItem('write_draft', JSON.stringify(formData))
    setAutoSaveStatus('임시 저장됨')
    setTimeout(() => setAutoSaveStatus(''), 2000)
  }

  const handleClearDraft = () => {
    if (confirm('임시 저장된 내용을 삭제하시겠습니까?')) {
      localStorage.removeItem('write_draft')
      setFormData({
        content: '',
        imageFile: null,
        location: '',
        weather: '',
        isPublic: true
      })
      setImagePreview(null)
      setAutoSaveStatus('임시 저장 삭제됨')
      setTimeout(() => setAutoSaveStatus(''), 2000)
    }
  }

  return (
    <div className="write-container">
      <div className="write-header">
        <h1>새로운 메모리</h1>
        <p>오늘의 특별한 순간을 기록해보세요</p>
        {autoSaveStatus && (
          <div className="auto-save-status badge-pastel">
            <FiSave size={14} /> {autoSaveStatus}
          </div>
        )}
      </div>

      <form onSubmit={handleSubmit} className="write-form card-glass">
        {/* 내용 입력 */}
        <div className="write-content-group">
          <textarea
            className="write-textarea input-glass"
            placeholder="무엇을 생각하고 계신가요?"
            value={formData.content}
            onChange={handleContentChange}
            disabled={isLoading}
            maxLength={1000}
          />
          <div className="write-char-count badge-pastel">
            {formData.content.length}/1000
          </div>
        </div>

        {/* 위치 및 날씨 */}
        <div className="write-meta-group">
          <div className="write-location-group">
            <label className="write-label">
              <FiMapPin size={16} /> 위치 (선택사항)
            </label>
            <input
              type="text"
              className="write-location-input input-glass"
              placeholder="어디에 계셨나요?"
              value={formData.location}
              onChange={(e) => setFormData(prev => ({ ...prev, location: e.target.value }))}
            />
          </div>
          
          <div className="write-weather-group">
            <label className="write-label">
              <FiSun size={16} /> 날씨 (선택사항)
            </label>
            <div className="weather-options">
              {WEATHER_OPTIONS.map((weather) => (
                <button
                  key={weather.value}
                  type="button"
                  className={`weather-btn ${formData.weather === weather.value ? 'selected' : ''}`}
                  onClick={() => setFormData(prev => ({ ...prev, weather: prev.weather === weather.value ? '' : weather.value }))}
                >
                  <span className="weather-icon">{weather.icon}</span>
                  <span className="weather-label">{weather.label.split(' ')[1]}</span>
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* 이미지 업로드 */}
        <div className="write-image-group">
          <label className="write-label">이미지 추가 (선택사항)</label>
          <div className="image-upload-area">
            {imagePreview ? (
              <div className="image-preview">
                <img src={imagePreview} alt="미리보기" />
                <button
                  type="button"
                  className="remove-image-btn btn-main"
                  onClick={removeImage}
                >
                  <FiX size={16} />
                </button>
              </div>
            ) : (
              <div
                className="image-upload-placeholder"
                onClick={() => fileInputRef.current?.click()}
              >
                <FiUpload size={32} />
                <p>이미지를 클릭하여 업로드하세요</p>
                <small>최대 5MB (JPG, PNG, GIF)</small>
              </div>
            )}
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              onChange={handleImageUpload}
              style={{ display: 'none' }}
            />
          </div>
        </div>

        {/* 공개 설정 */}
        <div className="write-privacy-group">
          <label className="write-label">공개 설정</label>
          <div className="privacy-toggle">
            <button
              type="button"
              className={`privacy-btn ${formData.isPublic ? 'active' : ''}`}
              onClick={() => setFormData(prev => ({ ...prev, isPublic: true }))}
            >
              <FiEye size={16} /> 공개
            </button>
            <button
              type="button"
              className={`privacy-btn ${!formData.isPublic ? 'active' : ''}`}
              onClick={() => setFormData(prev => ({ ...prev, isPublic: false }))}
            >
              <FiEyeOff size={16} /> 비공개
            </button>
          </div>
        </div>

        {/* 액션 버튼들 */}
        <div className="write-actions">
          <div className="write-secondary-actions">
            <button
              type="button"
              className="write-draft-btn btn-main"
              onClick={handleSaveDraft}
            >
              임시 저장
            </button>
            <button
              type="button"
              className="write-clear-btn btn-main"
              onClick={handleClearDraft}
            >
              임시 저장 삭제
            </button>
            <button
              type="button"
              className="write-preview-btn btn-main"
              onClick={() => setShowPreview(!showPreview)}
            >
              {showPreview ? '편집 모드' : '미리보기'}
            </button>
          </div>
          
          <div className="write-primary-actions">
            <button
              type="button"
              className="write-cancel-btn btn-main"
              onClick={() => navigate('/')}
              disabled={isLoading}
            >
              취소
            </button>
            <button
              type="submit"
              className="write-submit-btn btn-main"
              disabled={isLoading || !formData.content.trim()}
            >
              {isLoading ? '저장 중...' : '메모리 저장'}
            </button>
          </div>
        </div>
      </form>

      {/* 미리보기 모달 */}
      {showPreview && (
        <div className="write-preview-modal card-glass">
          <div className="preview-header">
            <h3>미리보기</h3>
            <button
              className="preview-close-btn btn-main"
              onClick={() => setShowPreview(false)}
            >
              <FiX size={20} />
            </button>
          </div>
          <div className="preview-content">
            <div className="preview-memory card-glass">
              <div className="preview-memory-header">
                <div className="preview-user-info">
                  <div className="preview-avatar">
                    {user?.profileImageUrl ? (
                      <img src={user.profileImageUrl} alt={user.username} />
                    ) : (
                      <div className="preview-avatar-placeholder">👤</div>
                    )}
                  </div>
                  <div className="preview-user-details">
                    <div className="preview-username">{user?.username}</div>
                    <div className="preview-date">방금 전</div>
                  </div>
                </div>
                <div className="preview-privacy">
                  {formData.isPublic ? <FiEye size={16} /> : <FiEyeOff size={16} />}
                </div>
              </div>
              
              <div className="preview-memory-content">
                <p>{formData.content}</p>
                {imagePreview && (
                  <div className="preview-image">
                    <img src={imagePreview} alt="미리보기" />
                  </div>
                )}
              </div>
              
              <div className="preview-memory-meta">
                {formData.location && (
                  <span className="preview-location badge-pastel">
                    <FiMapPin size={12} /> {formData.location}
                  </span>
                )}
                {formData.weather && (
                  <span className="preview-weather badge-pastel">
                    {WEATHER_OPTIONS.find(w => w.value === formData.weather)?.label}
                  </span>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
} 