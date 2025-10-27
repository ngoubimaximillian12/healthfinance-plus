package com.healthfinance.billingservice.service;

import com.healthfinance.billingservice.client.InsuranceClient;
import com.healthfinance.billingservice.model.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoClaimService {
    
    private final InsuranceClient insuranceClient;
    
    @Async
    public void submitInsuranceClaim(Invoice invoice) {
        log.info("Auto-submitting insurance claim for invoice: {}", invoice.getId());
        
        try {
            // Get patient's active insurance policy
            Map<String, Object> policy = insuranceClient.getActivePolicyByPatient(invoice.getPatientId());
            
            if (policy != null && !policy.containsKey("error")) {
                // Create claim
                Map<String, Object> claim = new HashMap<>();
                claim.put("policyId", policy.get("id"));
                claim.put("patientId", invoice.getPatientId());
                claim.put("claimNumber", "CLM-" + System.currentTimeMillis());
                claim.put("claimDate", LocalDate.now().toString());
                claim.put("serviceDate", invoice.getServiceDate().toString());
                claim.put("claimedAmount", invoice.getTotalAmount());
                claim.put("diagnosisCode", "ICD10-PLACEHOLDER");
                claim.put("procedureCode", "CPT-PLACEHOLDER");
                claim.put("providerName", "HealthFinance Plus");
                claim.put("status", "SUBMITTED");
                claim.put("notes", "Auto-submitted claim for invoice: " + invoice.getInvoiceNumber());
                
                // Submit claim
                Map<String, Object> result = insuranceClient.submitClaim(claim);
                log.info("Insurance claim submitted successfully: {}", result);
            } else {
                log.warn("No active insurance policy found for patient: {}", invoice.getPatientId());
            }
        } catch (Exception e) {
            log.error("Failed to submit insurance claim", e);
        }
    }
}
