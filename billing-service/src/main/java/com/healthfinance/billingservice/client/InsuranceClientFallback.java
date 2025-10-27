package com.healthfinance.billingservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InsuranceClientFallback implements InsuranceClient {
    
    @Override
    public Map<String, Object> getPolicyById(String id) {
        log.error("Insurance service unavailable - getPolicyById");
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Insurance service unavailable");
        return response;
    }
    
    @Override
    public Map<String, Object> submitClaim(Map<String, Object> claim) {
        log.error("Insurance service unavailable - submitClaim");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "PENDING");
        response.put("message", "Insurance service unavailable, claim will be submitted later");
        return response;
    }
    
    @Override
    public Map<String, Object> getActivePolicyByPatient(String patientId) {
        log.error("Insurance service unavailable - getActivePolicyByPatient");
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Insurance service unavailable");
        return response;
    }
}
