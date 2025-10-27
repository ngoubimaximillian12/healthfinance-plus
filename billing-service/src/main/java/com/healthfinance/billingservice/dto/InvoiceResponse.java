package com.healthfinance.billingservice.dto;

import com.healthfinance.billingservice.model.Invoice;
import com.healthfinance.billingservice.model.InvoiceStatus;
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
public class InvoiceResponse {
    
    private String id;
    private String invoiceNumber;
    private String patientId;
    private String appointmentId;
    private String insurancePolicyId;
    private String insuranceClaimId;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientResponsibility;
    private BigDecimal amountPaid;
    private BigDecimal amountDue;
    private String description;
    private String notes;
    private LocalDate paidDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static InvoiceResponse fromInvoice(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .patientId(invoice.getPatientId())
                .appointmentId(invoice.getAppointmentId())
                .insurancePolicyId(invoice.getInsurancePolicyId())
                .insuranceClaimId(invoice.getInsuranceClaimId())
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .status(invoice.getStatus())
                .subtotal(invoice.getSubtotal())
                .taxAmount(invoice.getTaxAmount())
                .discountAmount(invoice.getDiscountAmount())
                .totalAmount(invoice.getTotalAmount())
                .insuranceCoverage(invoice.getInsuranceCoverage())
                .patientResponsibility(invoice.getPatientResponsibility())
                .amountPaid(invoice.getAmountPaid())
                .amountDue(invoice.getAmountDue())
                .description(invoice.getDescription())
                .notes(invoice.getNotes())
                .paidDate(invoice.getPaidDate())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}
