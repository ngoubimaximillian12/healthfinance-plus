package com.healthfinance.appointmentservice.service;

import com.healthfinance.appointmentservice.client.NotificationClient;
import com.healthfinance.appointmentservice.model.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentNotificationService {
    
    private final NotificationClient notificationClient;
    
    @Async
    public void sendAppointmentConfirmation(Appointment appointment, String patientEmail) {
        log.info("Sending appointment confirmation for: {}", appointment.getId());
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("recipientId", appointment.getPatientId());
        notification.put("recipientEmail", patientEmail);
        notification.put("recipientName", "Patient");
        notification.put("notificationType", "APPOINTMENT_CONFIRMATION");
        notification.put("channel", "EMAIL");
        notification.put("subject", "Appointment Confirmed - " + appointment.getAppointmentDate());
        notification.put("message", String.format(
            "Your appointment has been confirmed for %s at %s. Duration: %d minutes.",
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getDurationMinutes()
        ));
        notification.put("relatedEntityId", appointment.getId());
        notification.put("relatedEntityType", "APPOINTMENT");
        
        try {
            notificationClient.sendNotification(notification);
            log.info("Appointment confirmation sent successfully");
        } catch (Exception e) {
            log.error("Failed to send appointment confirmation", e);
        }
    }
    
    @Async
    public void sendAppointmentReminder(Appointment appointment, String patientEmail) {
        log.info("Sending appointment reminder for: {}", appointment.getId());
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("recipientId", appointment.getPatientId());
        notification.put("recipientEmail", patientEmail);
        notification.put("recipientName", "Patient");
        notification.put("notificationType", "APPOINTMENT_REMINDER");
        notification.put("channel", "EMAIL");
        notification.put("subject", "Appointment Reminder - Tomorrow");
        notification.put("message", String.format(
            "Reminder: You have an appointment tomorrow at %s. Please arrive 15 minutes early.",
            appointment.getAppointmentTime()
        ));
        notification.put("relatedEntityId", appointment.getId());
        notification.put("relatedEntityType", "APPOINTMENT");
        
        try {
            notificationClient.sendNotification(notification);
            log.info("Appointment reminder sent successfully");
        } catch (Exception e) {
            log.error("Failed to send appointment reminder", e);
        }
    }
    
    @Async
    public void sendAppointmentCancellation(Appointment appointment, String patientEmail) {
        log.info("Sending appointment cancellation for: {}", appointment.getId());
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("recipientId", appointment.getPatientId());
        notification.put("recipientEmail", patientEmail);
        notification.put("recipientName", "Patient");
        notification.put("notificationType", "APPOINTMENT_CANCELLATION");
        notification.put("channel", "EMAIL");
        notification.put("subject", "Appointment Cancelled");
        notification.put("message", String.format(
            "Your appointment scheduled for %s at %s has been cancelled.",
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime()
        ));
        notification.put("relatedEntityId", appointment.getId());
        notification.put("relatedEntityType", "APPOINTMENT");
        
        try {
            notificationClient.sendNotification(notification);
            log.info("Appointment cancellation sent successfully");
        } catch (Exception e) {
            log.error("Failed to send appointment cancellation", e);
        }
    }
}
