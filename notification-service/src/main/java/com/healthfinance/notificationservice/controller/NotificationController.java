package com.healthfinance.notificationservice.controller;

import com.healthfinance.notificationservice.dto.NotificationRequest;
import com.healthfinance.notificationservice.dto.NotificationResponse;
import com.healthfinance.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Endpoints for managing notifications")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    @Operation(summary = "Create and send notification")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable String id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/recipient/{recipientId}")
    @Operation(summary = "Get notifications by recipient ID")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByRecipient(@PathVariable String recipientId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByRecipient(recipientId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Get all pending notifications")
    public ResponseEntity<List<NotificationResponse>> getPendingNotifications() {
        List<NotificationResponse> notifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping("/{id}/resend")
    @Operation(summary = "Resend notification")
    public ResponseEntity<Void> resendNotification(@PathVariable String id) {
        notificationService.resendNotification(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
