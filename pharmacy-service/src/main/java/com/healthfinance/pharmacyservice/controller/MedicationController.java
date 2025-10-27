package com.healthfinance.pharmacyservice.controller;

import com.healthfinance.pharmacyservice.dto.MedicationRequest;
import com.healthfinance.pharmacyservice.model.Medication;
import com.healthfinance.pharmacyservice.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy/medications")
@RequiredArgsConstructor
@Tag(name = "Medication Management")
public class MedicationController {
    
    private final MedicationService medicationService;
    
    @PostMapping
    @Operation(summary = "Create medication")
    public ResponseEntity<Medication> createMedication(@Valid @RequestBody MedicationRequest request) {
        Medication medication = medicationService.createMedication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(medication);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get medication by ID")
    public ResponseEntity<Medication> getMedicationById(@PathVariable String id) {
        Medication medication = medicationService.getMedicationById(id);
        return ResponseEntity.ok(medication);
    }
    
    @GetMapping
    @Operation(summary = "Get all medications")
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> medications = medicationService.getAllMedications();
        return ResponseEntity.ok(medications);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active medications")
    public ResponseEntity<List<Medication>> getActiveMedications() {
        List<Medication> medications = medicationService.getActiveMedications();
        return ResponseEntity.ok(medications);
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock medications")
    public ResponseEntity<List<Medication>> getLowStockMedications() {
        List<Medication> medications = medicationService.getLowStockMedications();
        return ResponseEntity.ok(medications);
    }
    
    @PutMapping("/{id}/stock")
    @Operation(summary = "Update medication stock")
    public ResponseEntity<Medication> updateStock(@PathVariable String id, 
                                                   @RequestBody Map<String, Integer> body) {
        Integer quantity = body.get("quantity");
        Medication updated = medicationService.updateStock(id, quantity);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medication")
    public ResponseEntity<Void> deleteMedication(@PathVariable String id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
