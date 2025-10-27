package com.healthfinance.insuranceservice.repository;

import com.healthfinance.insuranceservice.model.ClaimStatus;
import com.healthfinance.insuranceservice.model.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, String> {
    
    List<InsuranceClaim> findByUserId(String userId);
    
    List<InsuranceClaim> findByPolicyId(String policyId);
    
    List<InsuranceClaim> findByUserIdAndStatus(String userId, ClaimStatus status);
    
    Optional<InsuranceClaim> findByClaimNumber(String claimNumber);
    
    List<InsuranceClaim> findByStatus(ClaimStatus status);
    
    List<InsuranceClaim> findByServiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<InsuranceClaim> findByUserIdOrderBySubmissionDateDesc(String userId);
}
