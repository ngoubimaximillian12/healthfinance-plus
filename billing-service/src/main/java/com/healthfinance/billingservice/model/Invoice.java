package com.healthfinance.billingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String invoiceNumber;
    
    @Column(nullable = false)
    private String patientId;
    
    private String appointmentId;
    private String insurancePolicyId;
    private String insuranceClaimId;
    
    @Column(nullable = false)
    private LocalDate invoiceDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal insuranceCoverage;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal patientResponsibility;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amountPaid;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amountDue;
    
    @Column(length = 2000)
    private String description;
    
    @Column(length = 2000)
    private String notes;
    
    private LocalDate paidDate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
