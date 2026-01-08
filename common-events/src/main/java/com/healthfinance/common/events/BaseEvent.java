package com.healthfinance.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base implementation for all domain events.
 * Provides common fields and behavior for all events in the system.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance
     */
    private String eventId;

    /**
     * ID of the aggregate root that this event relates to
     */
    private String aggregateId;

    /**
     * Version of the aggregate (for event sourcing and optimistic locking)
     */
    private Integer version;

    /**
     * Timestamp when the event occurred
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Metadata about the event
     */
    private EventMetadata metadata;

    /**
     * Initialize event with defaults if not set
     */
    public void initializeDefaults() {
        if (this.eventId == null) {
            this.eventId = UUID.randomUUID().toString();
        }
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.version == null) {
            this.version = 1;
        }
        if (this.metadata == null) {
            this.metadata = EventMetadata.builder().build();
        }
    }

    /**
     * Get the event type from the class name
     */
    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }
}
