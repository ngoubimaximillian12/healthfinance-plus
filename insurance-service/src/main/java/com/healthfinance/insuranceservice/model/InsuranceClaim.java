package com.healthfinance.insuranceservice.model;

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
@Table(name = "insurance_claims")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String policyId;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false, unique = true)
    private String claimNumber;
    
    private String appointmentId;
    private String medicalRecordId;
    
    @Column(nullable = false)
    private LocalDate serviceDate;
    
    @Column(nullable = false)
    private LocalDate submissionDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;
    
    @Enumerated(EnumType.STRING)
    private CoverageType coverageType;
    
    @Column(nullable = false)
    private String providerName;
    
    private String providerId;
    
    @Column(nullable = false, length = 1000)
    private String serviceDescription;
    
    private String diagnosisCode;
    private String procedureCode;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal claimedAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal approvedAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal insurancePayment;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal patientResponsibility;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal copayAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deductibleAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal coinsuranceAmount;
    
    private LocalDate processedDate;
    private LocalDate paymentDate;
    
    @Column(length = 2000)
    private String denialReason;
    
    @Column(length = 2000)
    private String notes;
    
    @Column(length = 500)
    private String documentUrl;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
