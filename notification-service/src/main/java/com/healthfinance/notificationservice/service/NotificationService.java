package com.healthfinance.notificationservice.service;

import com.healthfinance.notificationservice.dto.NotificationRequest;
import com.healthfinance.notificationservice.dto.NotificationResponse;
import com.healthfinance.notificationservice.model.Notification;
import com.healthfinance.notificationservice.model.NotificationChannel;
import com.healthfinance.notificationservice.model.NotificationStatus;
import com.healthfinance.notificationservice.model.NotificationTemplate;
import com.healthfinance.notificationservice.repository.NotificationRepository;
import com.healthfinance.notificationservice.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        log.info("Creating notification for recipient: {}", request.getRecipientId());
        
        String subject = request.getSubject();
        String message = request.getMessage();
        String htmlContent = request.getHtmlContent();
        
        // If template is specified, use template
        if (request.getTemplateName() != null) {
            NotificationTemplate template = templateRepository.findByTemplateName(request.getTemplateName())
                    .orElseThrow(() -> new RuntimeException("Template not found: " + request.getTemplateName()));
            
            subject = processTemplate(template.getSubject(), request.getTemplateVariables());
            message = processTemplate(template.getBody(), request.getTemplateVariables());
            htmlContent = template.getHtmlBody() != null ? 
                    processTemplate(template.getHtmlBody(), request.getTemplateVariables()) : null;
        }
        
        Notification notification = Notification.builder()
                .recipientId(request.getRecipientId())
                .recipientEmail(request.getRecipientEmail())
                .recipientPhone(request.getRecipientPhone())
                .recipientName(request.getRecipientName())
                .notificationType(request.getNotificationType())
                .channel(request.getChannel())
                .status(NotificationStatus.PENDING)
                .subject(subject)
                .message(message)
                .htmlContent(htmlContent)
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .scheduledAt(request.getScheduledAt())
                .retryCount(0)
                .build();
        
        Notification saved = notificationRepository.save(notification);
        
        // Send immediately if not scheduled
        if (request.getScheduledAt() == null || request.getScheduledAt().isBefore(LocalDateTime.now())) {
            sendNotification(saved);
        }
        
        return NotificationResponse.fromNotification(saved);
    }
    
    @Transactional
    public void sendNotification(Notification notification) {
        try {
            switch (notification.getChannel()) {
                case EMAIL:
                    if (notification.getHtmlContent() != null) {
                        emailService.sendHtmlEmail(
                                notification.getRecipientEmail(),
                                notification.getSubject(),
                                notification.getHtmlContent()
                        );
                    } else {
                        emailService.sendSimpleEmail(
                                notification.getRecipientEmail(),
                                notification.getSubject(),
                                notification.getMessage()
                        );
                    }
                    break;
                    
                case SMS:
                    smsService.sendSms(
                            notification.getRecipientPhone(),
                            notification.getMessage()
                    );
                    break;
                    
                case PUSH:
                case IN_APP:
                    log.info("Push/In-app notifications not yet implemented");
                    break;
            }
            
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            
            log.info("Notification sent successfully: {}", notification.getId());
            
        } catch (Exception e) {
            log.error("Failed to send notification: {}", notification.getId(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
            notificationRepository.save(notification);
        }
    }
    
    private String processTemplate(String template, Map<String, String> variables) {
        if (variables == null || variables.isEmpty()) {
            return template;
        }
        
        String processed = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            processed = processed.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return processed;
    }
    
    public NotificationResponse getNotificationById(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return NotificationResponse.fromNotification(notification);
    }
    
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationResponse::fromNotification)
                .collect(Collectors.toList());
    }
    
    public List<NotificationResponse> getNotificationsByRecipient(String recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId).stream()
                .map(NotificationResponse::fromNotification)
                .collect(Collectors.toList());
    }
    
    public List<NotificationResponse> getPendingNotifications() {
        return notificationRepository.findByStatus(NotificationStatus.PENDING).stream()
                .map(NotificationResponse::fromNotification)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void resendNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setStatus(NotificationStatus.PENDING);
        notificationRepository.save(notification);
        
        sendNotification(notification);
    }
    
    @Transactional
    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }
}
