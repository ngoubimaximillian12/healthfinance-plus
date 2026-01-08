package com.healthfinance.appointmentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka configuration for appointment-service.
 * Defines topics and their configurations.
 */
@Configuration
public class KafkaConfig {

    public static final String APPOINTMENT_EVENTS_TOPIC = "appointment-events";
    public static final String PATIENT_EVENTS_TOPIC = "patient-events";
    public static final String CLINICAL_ALERTS_TOPIC = "clinical-alerts";

    /**
     * Create appointment events topic with 3 partitions for better throughput
     */
    @Bean
    public NewTopic appointmentEventsTopic() {
        return TopicBuilder.name(APPOINTMENT_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Create patient events topic
     */
    @Bean
    public NewTopic patientEventsTopic() {
        return TopicBuilder.name(PATIENT_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Create clinical alerts topic for urgent notifications
     */
    @Bean
    public NewTopic clinicalAlertsTopic() {
        return TopicBuilder.name(CLINICAL_ALERTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
