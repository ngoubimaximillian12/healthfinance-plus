package com.healthfinance.billingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "insurance-service", fallback = InsuranceClientFallback.class)
public interface InsuranceClient {
    
    @GetMapping("/api/insurance/policies/{id}")
    Map<String, Object> getPolicyById(@PathVariable String id);
    
    @PostMapping("/api/insurance/claims")
    Map<String, Object> submitClaim(@RequestBody Map<String, Object> claim);
    
    @GetMapping("/api/insurance/policies/patient/{patientId}/active")
    Map<String, Object> getActivePolicyByPatient(@PathVariable String patientId);
}
