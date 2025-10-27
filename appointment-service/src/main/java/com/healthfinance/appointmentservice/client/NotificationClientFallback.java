package com.healthfinance.appointmentservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class NotificationClientFallback implements NotificationClient {
    
    @Override
    public Map<String, Object> sendNotification(Map<String, Object> notification) {
        log.error("Notification service is unavailable. Notification not sent: {}", notification);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "FAILED");
        response.put("message", "Notification service unavailable");
        return response;
    }
}
