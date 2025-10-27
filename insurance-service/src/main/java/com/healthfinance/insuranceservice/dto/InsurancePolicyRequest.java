package com.healthfinance.insuranceservice.dto;

import com.healthfinance.insuranceservice.model.InsuranceType;
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
public class InsurancePolicyRequest {
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Policy number is required")
    private String policyNumber;
    
    @NotNull(message = "Insurance provider is required")
    private String insuranceProvider;
    
    private String insuranceProviderPhone;
    private String insuranceProviderEmail;
    private String insuranceProviderAddress;
    
    @NotNull(message = "Insurance type is required")
    private InsuranceType insuranceType;
    
    @NotNull(message = "Plan name is required")
    private String planName;
    
    private String groupNumber;
    private String subscriberId;
    private String subscriberName;
    
    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;
    
    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;
    
    private BigDecimal monthlyPremium;
    private BigDecimal deductible;
    private BigDecimal outOfPocketMax;
    private BigDecimal copayAmount;
    private BigDecimal coinsurancePercentage;
    private Boolean isPrimary;
    private String coverageDetails;
    private String notes;
}
