package com.healthfinance.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    
    @Value("${notification.sms.enabled}")
    private boolean smsEnabled;
    
    @Value("${twilio.account.sid:}")
    private String twilioAccountSid;
    
    @Value("${twilio.auth.token:}")
    private String twilioAuthToken;
    
    @Value("${twilio.phone.number:}")
    private String twilioPhoneNumber;
    
    @Async
    public void sendSms(String to, String message) {
        if (!smsEnabled) {
            log.info("SMS notifications disabled. Would have sent SMS to: {}", to);
            log.info("SMS Content: {}", message);
            return;
        }
        
        try {
            // In production, integrate with Twilio or similar SMS provider
            // For now, just log the SMS
            log.info("SMS sent to: {} - Message: {}", to, message);
            
            // Example Twilio integration (commented out):
            /*
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message twilioMessage = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
            log.info("SMS sent successfully. SID: {}", twilioMessage.getSid());
            */
            
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", to, e);
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}
