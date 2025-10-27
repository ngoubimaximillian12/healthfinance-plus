package com.healthfinance.prescriptionservice.service;

import com.healthfinance.prescriptionservice.client.PharmacyClient;
import com.healthfinance.prescriptionservice.model.Prescription;
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
public class AutoDispenseService {
    
    private final PharmacyClient pharmacyClient;
    
    @Async
    public void autoDispensePrescription(Prescription prescription) {
        log.info("Auto-dispensing prescription: {}", prescription.getId());
        
        try {
            // Create dispense order
            Map<String, Object> dispenseOrder = new HashMap<>();
            dispenseOrder.put("orderNumber", "DISP-AUTO-" + System.currentTimeMillis());
            dispenseOrder.put("prescriptionId", prescription.getId());
            dispenseOrder.put("patientId", prescription.getPatientId());
            dispenseOrder.put("medicationId", prescription.getMedicationName()); // In real app, map to actual medication ID
            dispenseOrder.put("quantityDispensed", prescription.getQuantity());
            dispenseOrder.put("refillsRemaining", prescription.getRefillsAllowed());
            dispenseOrder.put("dispenseDate", LocalDate.now().toString());
            dispenseOrder.put("dispensingInstructions", prescription.getInstructions());
            
            // Submit to pharmacy
            Map<String, Object> result = pharmacyClient.createDispenseOrder(dispenseOrder);
            log.info("Prescription auto-dispensed successfully: {}", result);
        } catch (Exception e) {
            log.error("Failed to auto-dispense prescription", e);
        }
    }
}
