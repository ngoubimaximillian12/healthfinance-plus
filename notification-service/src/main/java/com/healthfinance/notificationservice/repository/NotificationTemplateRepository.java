package com.healthfinance.notificationservice.repository;

import com.healthfinance.notificationservice.model.NotificationChannel;
import com.healthfinance.notificationservice.model.NotificationTemplate;
import com.healthfinance.notificationservice.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String> {
    
    Optional<NotificationTemplate> findByTemplateName(String templateName);
    
    List<NotificationTemplate> findByNotificationType(NotificationType notificationType);
    
    Optional<NotificationTemplate> findByNotificationTypeAndChannel(NotificationType notificationType, NotificationChannel channel);
    
    List<NotificationTemplate> findByIsActive(Boolean isActive);
}
