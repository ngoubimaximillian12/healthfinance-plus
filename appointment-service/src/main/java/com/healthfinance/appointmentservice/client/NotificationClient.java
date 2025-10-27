package com.healthfinance.appointmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", fallback = NotificationClientFallback.class)
public interface NotificationClient {
    
    @PostMapping("/api/notifications")
    Map<String, Object> sendNotification(@RequestBody Map<String, Object> notification);
}
