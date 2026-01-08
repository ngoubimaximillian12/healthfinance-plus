package com.healthfinance.common.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadata associated with domain events.
 * Used for tracing, auditing, and event correlation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMetadata {

    /**
     * Correlation ID for tracking related events across services
     */
    private String correlationId;

    /**
     * Causation ID - the event that caused this event
     */
    private String causationId;

    /**
     * User ID who triggered this event
     */
    private String userId;

    /**
     * IP address of the request that triggered this event
     */
    private String ipAddress;

    /**
     * User agent of the request
     */
    private String userAgent;

    /**
     * Service that published this event
     */
    private String publishingService;

    /**
     * Environment (dev, staging, production)
     */
    private String environment;
}
