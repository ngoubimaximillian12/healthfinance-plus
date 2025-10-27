package com.healthfinance.insuranceservice.repository;

import com.healthfinance.insuranceservice.model.InsurancePolicy;
import com.healthfinance.insuranceservice.model.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, String> {
    
    List<InsurancePolicy> findByUserId(String userId);
    
    List<InsurancePolicy> findByUserIdAndStatus(String userId, PolicyStatus status);
    
    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);
    
    List<InsurancePolicy> findByStatus(PolicyStatus status);
    
    List<InsurancePolicy> findByExpirationDateBefore(LocalDate date);
    
    List<InsurancePolicy> findByUserIdAndIsPrimary(String userId, Boolean isPrimary);
}
