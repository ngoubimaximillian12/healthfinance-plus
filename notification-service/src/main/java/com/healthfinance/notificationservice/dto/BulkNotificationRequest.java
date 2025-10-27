package com.healthfinance.notificationservice.dto;

import com.healthfinance.notificationservice.model.NotificationChannel;
import com.healthfinance.notificationservice.model.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkNotificationRequest {
    
    @NotNull(message = "Recipient IDs are required")
    private List<String> recipientIds;
    
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;
    
    @NotNull(message = "Subject is required")
    private String subject;
    
    @NotNull(message = "Message is required")
    private String message;
    
    private String htmlContent;
}
