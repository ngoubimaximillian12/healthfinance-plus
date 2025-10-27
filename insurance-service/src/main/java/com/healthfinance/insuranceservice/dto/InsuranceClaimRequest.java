package com.healthfinance.insuranceservice.dto;

import com.healthfinance.insuranceservice.model.CoverageType;
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
public class InsuranceClaimRequest {
    
    @NotNull(message = "Policy ID is required")
    private String policyId;
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Claim number is required")
    private String claimNumber;
    
    private String appointmentId;
    private String medicalRecordId;
    
    @NotNull(message = "Service date is required")
    private LocalDate serviceDate;
    
    @NotNull(message = "Submission date is required")
    private LocalDate submissionDate;
    
    private CoverageType coverageType;
    
    @NotNull(message = "Provider name is required")
    private String providerName;
    
    private String providerId;
    
    @NotNull(message = "Service description is required")
    private String serviceDescription;
    
    private String diagnosisCode;
    private String procedureCode;
    
    @NotNull(message = "Claimed amount is required")
    private BigDecimal claimedAmount;
    
    private String notes;
    private String documentUrl;
}
