package com.healthfinance.prescriptionservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PharmacyClientFallback implements PharmacyClient {
    
    @Override
    public List<Map<String, Object>> getAllMedications() {
        log.error("Pharmacy service unavailable - getAllMedications");
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getMedicationById(String id) {
        log.error("Pharmacy service unavailable - getMedicationById");
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Pharmacy service unavailable");
        return response;
    }
    
    @Override
    public Map<String, Object> createDispenseOrder(Map<String, Object> dispenseOrder) {
        log.error("Pharmacy service unavailable - createDispenseOrder");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "PENDING");
        response.put("message", "Pharmacy service unavailable, order will be processed later");
        return response;
    }
}
