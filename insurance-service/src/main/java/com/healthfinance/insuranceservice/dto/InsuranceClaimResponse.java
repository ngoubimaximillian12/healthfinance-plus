package com.healthfinance.insuranceservice.dto;

import com.healthfinance.insuranceservice.model.ClaimStatus;
import com.healthfinance.insuranceservice.model.CoverageType;
import com.healthfinance.insuranceservice.model.InsuranceClaim;
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
public class InsuranceClaimResponse {
    
    private String id;
    private String policyId;
    private String userId;
    private String claimNumber;
    private String appointmentId;
    private String medicalRecordId;
    private LocalDate serviceDate;
    private LocalDate submissionDate;
    private ClaimStatus status;
    private CoverageType coverageType;
    private String providerName;
    private String providerId;
    private String serviceDescription;
    private String diagnosisCode;
    private String procedureCode;
    private BigDecimal claimedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal insurancePayment;
    private BigDecimal patientResponsibility;
    private BigDecimal copayAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal coinsuranceAmount;
    private LocalDate processedDate;
    private LocalDate paymentDate;
    private String denialReason;
    private String notes;
    private String documentUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static InsuranceClaimResponse fromClaim(InsuranceClaim claim) {
        return InsuranceClaimResponse.builder()
                .id(claim.getId())
                .policyId(claim.getPolicyId())
                .userId(claim.getUserId())
                .claimNumber(claim.getClaimNumber())
                .appointmentId(claim.getAppointmentId())
                .medicalRecordId(claim.getMedicalRecordId())
                .serviceDate(claim.getServiceDate())
                .submissionDate(claim.getSubmissionDate())
                .status(claim.getStatus())
                .coverageType(claim.getCoverageType())
                .providerName(claim.getProviderName())
                .providerId(claim.getProviderId())
                .serviceDescription(claim.getServiceDescription())
                .diagnosisCode(claim.getDiagnosisCode())
                .procedureCode(claim.getProcedureCode())
                .claimedAmount(claim.getClaimedAmount())
                .approvedAmount(claim.getApprovedAmount())
                .insurancePayment(claim.getInsurancePayment())
                .patientResponsibility(claim.getPatientResponsibility())
                .copayAmount(claim.getCopayAmount())
                .deductibleAmount(claim.getDeductibleAmount())
                .coinsuranceAmount(claim.getCoinsuranceAmount())
                .processedDate(claim.getProcessedDate())
                .paymentDate(claim.getPaymentDate())
                .denialReason(claim.getDenialReason())
                .notes(claim.getNotes())
                .documentUrl(claim.getDocumentUrl())
                .createdAt(claim.getCreatedAt())
                .updatedAt(claim.getUpdatedAt())
                .build();
    }
}
