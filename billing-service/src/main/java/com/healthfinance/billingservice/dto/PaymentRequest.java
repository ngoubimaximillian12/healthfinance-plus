package com.healthfinance.billingservice.dto;

import com.healthfinance.billingservice.model.PaymentMethod;
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
public class PaymentRequest {
    
    @NotNull(message = "Transaction ID is required")
    private String transactionId;
    
    @NotNull(message = "Invoice ID is required")
    private String invoiceId;
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    private String cardLast4Digits;
    private String cardType;
    private String checkNumber;
    private String referenceNumber;
    private String notes;
}
