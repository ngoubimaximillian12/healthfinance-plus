package com.healthfinance.common.events;

import java.time.LocalDateTime;

/**
 * Base interface for all domain events in the system.
 * Domain events represent significant business occurrences that have happened.
 */
public interface DomainEvent {

    /**
     * Unique identifier for this event instance
     */
    String getEventId();

    /**
     * Type of the event (e.g., "AppointmentScheduled", "PatientRegistered")
     */
    String getEventType();

    /**
     * Timestamp when the event occurred
     */
    LocalDateTime getTimestamp();

    /**
     * ID of the aggregate root that this event relates to
     */
    String getAggregateId();

    /**
     * Version of the aggregate (for event sourcing)
     */
    Integer getVersion();

    /**
     * Metadata about the event (correlation ID, causation ID, user ID, etc.)
     */
    EventMetadata getMetadata();
}
