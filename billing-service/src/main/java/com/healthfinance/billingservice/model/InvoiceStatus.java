package com.healthfinance.billingservice.model;

public enum InvoiceStatus {
    DRAFT,
    SENT,
    PENDING,
    PAID,
    PARTIALLY_PAID,
    OVERDUE,
    CANCELLED,
    REFUNDED
}
