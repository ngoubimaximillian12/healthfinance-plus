package com.healthfinance.pharmacyservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseOrderRequest {
    
    @NotNull(message = "Order number is required")
    private String orderNumber;
    
    @NotNull(message = "Prescription ID is required")
    private String prescriptionId;
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    @NotNull(message = "Medication ID is required")
    private String medicationId;
    
    @NotNull(message = "Quantity is required")
    private Integer quantityDispensed;
    
    private Integer refillsRemaining;
    
    @NotNull(message = "Dispense date is required")
    private LocalDate dispenseDate;
    
    private String pharmacistId;
    private String pharmacistName;
    private BigDecimal totalCost;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientCopay;
    private String dispensingInstructions;
    private String counselingNotes;
}
