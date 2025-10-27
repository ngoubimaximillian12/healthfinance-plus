package com.healthfinance.notificationservice.repository;

import com.healthfinance.notificationservice.model.Notification;
import com.healthfinance.notificationservice.model.NotificationStatus;
import com.healthfinance.notificationservice.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    
    List<Notification> findByRecipientId(String recipientId);
    
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
    
    List<Notification> findByStatus(NotificationStatus status);
    
    List<Notification> findByNotificationType(NotificationType notificationType);
    
    List<Notification> findByRecipientIdAndStatus(String recipientId, NotificationStatus status);
    
    @Query("SELECT n FROM Notification n WHERE n.scheduledAt <= :now AND n.status = 'PENDING'")
    List<Notification> findScheduledNotifications(@Param("now") LocalDateTime now);
    
    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED' AND n.retryCount < :maxRetries")
    List<Notification> findFailedNotificationsForRetry(@Param("maxRetries") int maxRetries);
    
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
