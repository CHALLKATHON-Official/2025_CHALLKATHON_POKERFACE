package com.time.PokerFace.notification.service;

import com.time.PokerFace.notification.dto.NotificationResponse;
import com.time.PokerFace.notification.entity.Notification;
import com.time.PokerFace.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Long userId, Notification.NotificationType type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<NotificationResponse> responses = new ArrayList<>();
        for (Notification notification : notifications) {
            responses.add(toResponse(notification));
        }
        return responses;
    }

    public void markAsRead(Long notificationId, Long userId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            if (notification.getUserId().equals(userId)) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        }
    }

    public void deleteNotification(Long notificationId, Long userId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            if (notification.getUserId().equals(userId)) {
                notificationRepository.delete(notification);
            }
        }
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setType(notification.getType().name());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setRead(notification.isRead());
        response.setRelatedId(notification.getRelatedId());
        response.setCreatedAt(notification.getCreatedAt() != null ? 
            notification.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        return response;
    }
} 