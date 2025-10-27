package com.healthfinance.billingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    
    @NotNull(message = "Invoice number is required")
    private String invoiceNumber;
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    private String appointmentId;
    private String insurancePolicyId;
    private String insuranceClaimId;
    
    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal insuranceCoverage;
    private String description;
    private String notes;
    
    private List<InvoiceItemRequest> items;
}
