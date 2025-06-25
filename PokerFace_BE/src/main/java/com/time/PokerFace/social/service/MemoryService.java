package com.time.PokerFace.social.service;

import com.time.PokerFace.social.dto.MemoryUploadRequest;
import com.time.PokerFace.social.dto.MemoryResponse;
import com.time.PokerFace.social.entity.Emotion;
import com.time.PokerFace.social.entity.Memory;
import com.time.PokerFace.social.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final S3Uploader s3Uploader;

    @Autowired
    public MemoryService(MemoryRepository memoryRepository, S3Uploader s3Uploader) {
        this.memoryRepository = memoryRepository;
        this.s3Uploader = s3Uploader;
    }

    public MemoryResponse uploadMemory(Long userId, MemoryUploadRequest request) throws IOException {
        String imageUrl = null;
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Uploader.upload(image, "memories");
        }

        Emotion emotion = Emotion.valueOf(request.getEmotion().toUpperCase());

        Memory memory = new Memory();
        memory.setUserId(userId);
        memory.setContent(request.getContent());
        memory.setEmotion(emotion);
        memory.setImageUrl(imageUrl);

        Memory saved = memoryRepository.save(memory);

        MemoryResponse response = new MemoryResponse();
        response.setId(saved.getId());
        response.setContent(saved.getContent());
        response.setEmotion(saved.getEmotion().name());
        response.setImageUrl(saved.getImageUrl());
        response.setCreatedAt(saved.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
} 