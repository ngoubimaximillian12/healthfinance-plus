package com.healthfinance.insuranceservice.dto;

import com.healthfinance.insuranceservice.model.InsurancePolicy;
import com.healthfinance.insuranceservice.model.InsuranceType;
import com.healthfinance.insuranceservice.model.PolicyStatus;
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
public class InsurancePolicyResponse {
    
    private String id;
    private String userId;
    private String policyNumber;
    private String insuranceProvider;
    private String insuranceProviderPhone;
    private String insuranceProviderEmail;
    private String insuranceProviderAddress;
    private InsuranceType insuranceType;
    private String planName;
    private String groupNumber;
    private String subscriberId;
    private String subscriberName;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private PolicyStatus status;
    private BigDecimal monthlyPremium;
    private BigDecimal deductible;
    private BigDecimal deductibleMet;
    private BigDecimal outOfPocketMax;
    private BigDecimal outOfPocketMet;
    private BigDecimal copayAmount;
    private BigDecimal coinsurancePercentage;
    private Boolean isPrimary;
    private String coverageDetails;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static InsurancePolicyResponse fromPolicy(InsurancePolicy policy) {
        return InsurancePolicyResponse.builder()
                .id(policy.getId())
                .userId(policy.getUserId())
                .policyNumber(policy.getPolicyNumber())
                .insuranceProvider(policy.getInsuranceProvider())
                .insuranceProviderPhone(policy.getInsuranceProviderPhone())
                .insuranceProviderEmail(policy.getInsuranceProviderEmail())
                .insuranceProviderAddress(policy.getInsuranceProviderAddress())
                .insuranceType(policy.getInsuranceType())
                .planName(policy.getPlanName())
                .groupNumber(policy.getGroupNumber())
                .subscriberId(policy.getSubscriberId())
                .subscriberName(policy.getSubscriberName())
                .effectiveDate(policy.getEffectiveDate())
                .expirationDate(policy.getExpirationDate())
                .status(policy.getStatus())
                .monthlyPremium(policy.getMonthlyPremium())
                .deductible(policy.getDeductible())
                .deductibleMet(policy.getDeductibleMet())
                .outOfPocketMax(policy.getOutOfPocketMax())
                .outOfPocketMet(policy.getOutOfPocketMet())
                .copayAmount(policy.getCopayAmount())
                .coinsurancePercentage(policy.getCoinsurancePercentage())
                .isPrimary(policy.getIsPrimary())
                .coverageDetails(policy.getCoverageDetails())
                .notes(policy.getNotes())
                .createdAt(policy.getCreatedAt())
                .updatedAt(policy.getUpdatedAt())
                .build();
    }
}
