package com.healthfinance.prescriptionservice.controller;

import com.healthfinance.prescriptionservice.dto.PrescriptionRequest;
import com.healthfinance.prescriptionservice.dto.PrescriptionResponse;
import com.healthfinance.prescriptionservice.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription Management", description = "Endpoints for managing prescriptions")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;
    
    @PostMapping
    @Operation(summary = "Create new prescription")
    public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        PrescriptionResponse response = prescriptionService.createPrescription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(@PathVariable String id) {
        PrescriptionResponse response = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all prescriptions")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptions() {
        List<PrescriptionResponse> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get prescriptions by patient ID")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByPatient(@PathVariable String patientId) {
        List<PrescriptionResponse> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/patient/{patientId}/active")
    @Operation(summary = "Get active prescriptions by patient ID")
    public ResponseEntity<List<PrescriptionResponse>> getActivePrescriptionsByPatient(@PathVariable String patientId) {
        List<PrescriptionResponse> prescriptions = prescriptionService.getActivePrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get prescriptions by doctor ID")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByDoctor(@PathVariable String doctorId) {
        List<PrescriptionResponse> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctorId);
        return ResponseEntity.ok(prescriptions);
    }
    
    @PostMapping("/{id}/refill")
    @Operation(summary = "Refill prescription")
    public ResponseEntity<PrescriptionResponse> refillPrescription(@PathVariable String id) {
        PrescriptionResponse response = prescriptionService.refillPrescription(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel prescription")
    public ResponseEntity<Void> cancelPrescription(@PathVariable String id) {
        prescriptionService.cancelPrescription(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete prescription")
    public ResponseEntity<Void> deletePrescription(@PathVariable String id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
