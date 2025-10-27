package com.healthfinance.insuranceservice.service;

import com.healthfinance.insuranceservice.dto.InsuranceClaimRequest;
import com.healthfinance.insuranceservice.dto.InsuranceClaimResponse;
import com.healthfinance.insuranceservice.model.ClaimStatus;
import com.healthfinance.insuranceservice.model.InsuranceClaim;
import com.healthfinance.insuranceservice.repository.InsuranceClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceClaimService {
    
    private final InsuranceClaimRepository claimRepository;
    
    @Transactional
    public InsuranceClaimResponse createClaim(InsuranceClaimRequest request) {
        log.info("Creating insurance claim for user: {}", request.getUserId());
        
        InsuranceClaim claim = InsuranceClaim.builder()
                .policyId(request.getPolicyId())
                .userId(request.getUserId())
                .claimNumber(request.getClaimNumber())
                .appointmentId(request.getAppointmentId())
                .medicalRecordId(request.getMedicalRecordId())
                .serviceDate(request.getServiceDate())
                .submissionDate(request.getSubmissionDate())
                .status(ClaimStatus.SUBMITTED)
                .coverageType(request.getCoverageType())
                .providerName(request.getProviderName())
                .providerId(request.getProviderId())
                .serviceDescription(request.getServiceDescription())
                .diagnosisCode(request.getDiagnosisCode())
                .procedureCode(request.getProcedureCode())
                .claimedAmount(request.getClaimedAmount())
                .notes(request.getNotes())
                .documentUrl(request.getDocumentUrl())
                .build();
        
        InsuranceClaim saved = claimRepository.save(claim);
        log.info("Claim created with ID: {}", saved.getId());
        
        return InsuranceClaimResponse.fromClaim(saved);
    }
    
    public InsuranceClaimResponse getClaimById(String id) {
        InsuranceClaim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        return InsuranceClaimResponse.fromClaim(claim);
    }
    
    public List<InsuranceClaimResponse> getAllClaims() {
        return claimRepository.findAll().stream()
                .map(InsuranceClaimResponse::fromClaim)
                .collect(Collectors.toList());
    }
    
    public List<InsuranceClaimResponse> getClaimsByUser(String userId) {
        return claimRepository.findByUserIdOrderBySubmissionDateDesc(userId).stream()
                .map(InsuranceClaimResponse::fromClaim)
                .collect(Collectors.toList());
    }
    
    public List<InsuranceClaimResponse> getClaimsByPolicy(String policyId) {
        return claimRepository.findByPolicyId(policyId).stream()
                .map(InsuranceClaimResponse::fromClaim)
                .collect(Collectors.toList());
    }
    
    public List<InsuranceClaimResponse> getClaimsByStatus(String userId, ClaimStatus status) {
        return claimRepository.findByUserIdAndStatus(userId, status).stream()
                .map(InsuranceClaimResponse::fromClaim)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public InsuranceClaimResponse approveClaim(String id, BigDecimal approvedAmount, 
                                               BigDecimal insurancePayment, 
                                               BigDecimal patientResponsibility) {
        InsuranceClaim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        
        claim.setStatus(ClaimStatus.APPROVED);
        claim.setApprovedAmount(approvedAmount);
        claim.setInsurancePayment(insurancePayment);
        claim.setPatientResponsibility(patientResponsibility);
        claim.setProcessedDate(LocalDate.now());
        
        InsuranceClaim updated = claimRepository.save(claim);
        return InsuranceClaimResponse.fromClaim(updated);
    }
    
    @Transactional
    public InsuranceClaimResponse denyClaim(String id, String denialReason) {
        InsuranceClaim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        
        claim.setStatus(ClaimStatus.DENIED);
        claim.setDenialReason(denialReason);
        claim.setProcessedDate(LocalDate.now());
        
        InsuranceClaim updated = claimRepository.save(claim);
        return InsuranceClaimResponse.fromClaim(updated);
    }
    
    @Transactional
    public void deleteClaim(String id) {
        claimRepository.deleteById(id);
    }
}
