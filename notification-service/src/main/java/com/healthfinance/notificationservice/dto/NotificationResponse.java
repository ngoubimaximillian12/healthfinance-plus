package com.healthfinance.notificationservice.dto;

import com.healthfinance.notificationservice.model.Notification;
import com.healthfinance.notificationservice.model.NotificationChannel;
import com.healthfinance.notificationservice.model.NotificationStatus;
import com.healthfinance.notificationservice.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    
    private String id;
    private String recipientId;
    private String recipientEmail;
    private String recipientPhone;
    private String recipientName;
    private NotificationType notificationType;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String subject;
    private String message;
    private String relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private String errorMessage;
    private Integer retryCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static NotificationResponse fromNotification(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipientId())
                .recipientEmail(notification.getRecipientEmail())
                .recipientPhone(notification.getRecipientPhone())
                .recipientName(notification.getRecipientName())
                .notificationType(notification.getNotificationType())
                .channel(notification.getChannel())
                .status(notification.getStatus())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .scheduledAt(notification.getScheduledAt())
                .sentAt(notification.getSentAt())
                .deliveredAt(notification.getDeliveredAt())
                .errorMessage(notification.getErrorMessage())
                .retryCount(notification.getRetryCount())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
