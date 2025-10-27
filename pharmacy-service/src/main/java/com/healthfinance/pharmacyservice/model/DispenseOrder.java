package com.healthfinance.pharmacyservice.model;

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
@Table(name = "dispense_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @Column(nullable = false)
    private String prescriptionId;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String medicationId;
    
    @Column(nullable = false)
    private Integer quantityDispensed;
    
    private Integer refillsRemaining;
    
    @Column(nullable = false)
    private LocalDate dispenseDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DispenseStatus status;
    
    private String pharmacistId;
    private String pharmacistName;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal insuranceCoverage;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal patientCopay;
    
    @Column(length = 2000)
    private String dispensingInstructions;
    
    @Column(length = 1000)
    private String counselingNotes;
    
    private LocalDateTime pickupDateTime;
    
    @Column(length = 500)
    private String pickupSignature;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
