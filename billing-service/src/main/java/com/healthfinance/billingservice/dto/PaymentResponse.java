package com.healthfinance.billingservice.dto;

import com.healthfinance.billingservice.model.Payment;
import com.healthfinance.billingservice.model.PaymentMethod;
import com.healthfinance.billingservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private String id;
    private String transactionId;
    private String invoiceId;
    private String patientId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String cardLast4Digits;
    private String cardType;
    private String checkNumber;
    private String referenceNumber;
    private String notes;
    private LocalDate processedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static PaymentResponse fromPayment(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .invoiceId(payment.getInvoiceId())
                .patientId(payment.getPatientId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .cardLast4Digits(payment.getCardLast4Digits())
                .cardType(payment.getCardType())
                .checkNumber(payment.getCheckNumber())
                .referenceNumber(payment.getReferenceNumber())
                .notes(payment.getNotes())
                .processedDate(payment.getProcessedDate())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
