package com.healthfinance.appointmentservice.event;

import com.healthfinance.common.events.DomainEvent;
import com.healthfinance.common.events.EventMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for publishing domain events to Kafka.
 * Handles event initialization, metadata population, and asynchronous publishing.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publish a domain event to the specified Kafka topic
     *
     * @param topic The Kafka topic to publish to
     * @param event The domain event to publish
     * @return CompletableFuture with the send result
     */
    public CompletableFuture<SendResult<String, Object>> publishEvent(String topic, DomainEvent event) {
        // Initialize event defaults if not set
        if (event instanceof com.healthfinance.common.events.BaseEvent) {
            ((com.healthfinance.common.events.BaseEvent) event).initializeDefaults();
        }

        // Enrich metadata
        enrichMetadata(event);

        log.info("Publishing event: {} to topic: {} with eventId: {}",
                event.getEventType(), topic, event.getEventId());

        // Publish to Kafka with the aggregate ID as the key for partitioning
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, event.getAggregateId(), event);

        // Add success and failure callbacks
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event: {} with eventId: {}. Error: {}",
                        event.getEventType(), event.getEventId(), ex.getMessage(), ex);
            } else {
                log.info("Successfully published event: {} with eventId: {} to partition: {}",
                        event.getEventType(), event.getEventId(),
                        result.getRecordMetadata().partition());
            }
        });

        return future;
    }

    /**
     * Enrich event metadata with service-specific information
     */
    private void enrichMetadata(DomainEvent event) {
        EventMetadata metadata = event.getMetadata();
        if (metadata == null) {
            metadata = EventMetadata.builder().build();
        }

        // Set publishing service
        if (metadata.getPublishingService() == null) {
            metadata.setPublishingService("appointment-service");
        }

        // Set environment (could be read from properties)
        if (metadata.getEnvironment() == null) {
            metadata.setEnvironment(System.getProperty("spring.profiles.active", "dev"));
        }

        // Note: In a real implementation, you would also set:
        // - correlationId from request context
        // - userId from security context
        // - ipAddress from request
    }
}
