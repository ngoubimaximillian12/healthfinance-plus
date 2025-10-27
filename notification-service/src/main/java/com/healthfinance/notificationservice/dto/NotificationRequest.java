package com.healthfinance.notificationservice.dto;

import com.healthfinance.notificationservice.model.NotificationChannel;
import com.healthfinance.notificationservice.model.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    
    @NotNull(message = "Recipient ID is required")
    private String recipientId;
    
    @NotNull(message = "Recipient email is required")
    private String recipientEmail;
    
    private String recipientPhone;
    private String recipientName;
    
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;
    
    @NotNull(message = "Subject is required")
    private String subject;
    
    @NotNull(message = "Message is required")
    private String message;
    
    private String htmlContent;
    private String relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime scheduledAt;
    
    // For template-based notifications
    private String templateName;
    private Map<String, String> templateVariables;
}
