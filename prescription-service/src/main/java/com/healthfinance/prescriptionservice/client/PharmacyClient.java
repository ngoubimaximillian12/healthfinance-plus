package com.healthfinance.prescriptionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "pharmacy-service", fallback = PharmacyClientFallback.class)
public interface PharmacyClient {
    
    @GetMapping("/api/pharmacy/medications")
    List<Map<String, Object>> getAllMedications();
    
    @GetMapping("/api/pharmacy/medications/{id}")
    Map<String, Object> getMedicationById(@PathVariable String id);
    
    @PostMapping("/api/pharmacy/dispense")
    Map<String, Object> createDispenseOrder(@RequestBody Map<String, Object> dispenseOrder);
}
