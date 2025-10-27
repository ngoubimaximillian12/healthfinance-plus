package com.healthfinance.pharmacyservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    
    @NotNull(message = "Medication ID is required")
    private String medicationId;
    
    @NotNull(message = "Transaction type is required")
    private String transactionType;
    
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    
    private String referenceNumber;
    private String notes;
    private String performedBy;
}
