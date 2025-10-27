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
@Table(name = "insurance_policies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false, unique = true)
    private String policyNumber;
    
    @Column(nullable = false)
    private String insuranceProvider;
    
    private String insuranceProviderPhone;
    private String insuranceProviderEmail;
    private String insuranceProviderAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceType insuranceType;
    
    @Column(nullable = false)
    private String planName;
    
    private String groupNumber;
    private String subscriberId;
    private String subscriberName;
    
    @Column(nullable = false)
    private LocalDate effectiveDate;
    
    @Column(nullable = false)
    private LocalDate expirationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyPremium;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deductible;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deductibleMet;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal outOfPocketMax;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal outOfPocketMet;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal copayAmount;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal coinsurancePercentage;
    
    private Boolean isPrimary;
    
    @Column(length = 2000)
    private String coverageDetails;
    
    @Column(length = 1000)
    private String notes;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
