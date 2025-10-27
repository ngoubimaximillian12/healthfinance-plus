package com.healthfinance.billingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {
    
    @NotNull(message = "Service code is required")
    private String serviceCode;
    
    @NotNull(message = "Service name is required")
    private String serviceName;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
    
    private BigDecimal discountAmount;
}
