package com.healthfinance.insuranceservice.service;

import com.healthfinance.insuranceservice.dto.InsurancePolicyRequest;
import com.healthfinance.insuranceservice.dto.InsurancePolicyResponse;
import com.healthfinance.insuranceservice.model.InsurancePolicy;
import com.healthfinance.insuranceservice.model.PolicyStatus;
import com.healthfinance.insuranceservice.repository.InsurancePolicyRepository;
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
public class InsurancePolicyService {
    
    private final InsurancePolicyRepository policyRepository;
    
    @Transactional
    public InsurancePolicyResponse createPolicy(InsurancePolicyRequest request) {
        log.info("Creating insurance policy for user: {}", request.getUserId());
        
        PolicyStatus status = LocalDate.now().isBefore(request.getEffectiveDate()) 
                ? PolicyStatus.PENDING 
                : PolicyStatus.ACTIVE;
        
        InsurancePolicy policy = InsurancePolicy.builder()
                .userId(request.getUserId())
                .policyNumber(request.getPolicyNumber())
                .insuranceProvider(request.getInsuranceProvider())
                .insuranceProviderPhone(request.getInsuranceProviderPhone())
                .insuranceProviderEmail(request.getInsuranceProviderEmail())
                .insuranceProviderAddress(request.getInsuranceProviderAddress())
                .insuranceType(request.getInsuranceType())
                .planName(request.getPlanName())
                .groupNumber(request.getGroupNumber())
                .subscriberId(request.getSubscriberId())
                .subscriberName(request.getSubscriberName())
                .effectiveDate(request.getEffectiveDate())
                .expirationDate(request.getExpirationDate())
                .status(status)
                .monthlyPremium(request.getMonthlyPremium())
                .deductible(request.getDeductible())
                .deductibleMet(BigDecimal.ZERO)
                .outOfPocketMax(request.getOutOfPocketMax())
                .outOfPocketMet(BigDecimal.ZERO)
                .copayAmount(request.getCopayAmount())
                .coinsurancePercentage(request.getCoinsurancePercentage())
                .isPrimary(request.getIsPrimary())
                .coverageDetails(request.getCoverageDetails())
                .notes(request.getNotes())
                .build();
        
        InsurancePolicy saved = policyRepository.save(policy);
        log.info("Policy created with ID: {}", saved.getId());
        
        return InsurancePolicyResponse.fromPolicy(saved);
    }
    
    public InsurancePolicyResponse getPolicyById(String id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        return InsurancePolicyResponse.fromPolicy(policy);
    }
    
    public List<InsurancePolicyResponse> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(InsurancePolicyResponse::fromPolicy)
                .collect(Collectors.toList());
    }
    
    public List<InsurancePolicyResponse> getPoliciesByUser(String userId) {
        return policyRepository.findByUserId(userId).stream()
                .map(InsurancePolicyResponse::fromPolicy)
                .collect(Collectors.toList());
    }
    
    public List<InsurancePolicyResponse> getActivePoliciesByUser(String userId) {
        return policyRepository.findByUserIdAndStatus(userId, PolicyStatus.ACTIVE).stream()
                .map(InsurancePolicyResponse::fromPolicy)
                .collect(Collectors.toList());
    }
    
    public InsurancePolicyResponse getPrimaryPolicy(String userId) {
        List<InsurancePolicy> primaryPolicies = policyRepository.findByUserIdAndIsPrimary(userId, true);
        if (primaryPolicies.isEmpty()) {
            throw new RuntimeException("No primary policy found for user");
        }
        return InsurancePolicyResponse.fromPolicy(primaryPolicies.get(0));
    }
    
    @Transactional
    public InsurancePolicyResponse updatePolicy(String id, InsurancePolicyRequest request) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        
        policy.setInsuranceProvider(request.getInsuranceProvider());
        policy.setInsuranceProviderPhone(request.getInsuranceProviderPhone());
        policy.setInsuranceProviderEmail(request.getInsuranceProviderEmail());
        policy.setInsuranceProviderAddress(request.getInsuranceProviderAddress());
        policy.setPlanName(request.getPlanName());
        policy.setMonthlyPremium(request.getMonthlyPremium());
        policy.setDeductible(request.getDeductible());
        policy.setOutOfPocketMax(request.getOutOfPocketMax());
        policy.setCopayAmount(request.getCopayAmount());
        policy.setCoinsurancePercentage(request.getCoinsurancePercentage());
        policy.setCoverageDetails(request.getCoverageDetails());
        policy.setNotes(request.getNotes());
        
        InsurancePolicy updated = policyRepository.save(policy);
        return InsurancePolicyResponse.fromPolicy(updated);
    }
    
    @Transactional
    public void cancelPolicy(String id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        
        policy.setStatus(PolicyStatus.CANCELLED);
        policyRepository.save(policy);
    }
    
    @Transactional
    public void deletePolicy(String id) {
        policyRepository.deleteById(id);
    }
}
