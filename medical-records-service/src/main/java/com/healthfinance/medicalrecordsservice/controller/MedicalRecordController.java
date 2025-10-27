package com.healthfinance.medicalrecordsservice.controller;

import com.healthfinance.medicalrecordsservice.dto.MedicalRecordRequest;
import com.healthfinance.medicalrecordsservice.dto.MedicalRecordResponse;
import com.healthfinance.medicalrecordsservice.model.RecordType;
import com.healthfinance.medicalrecordsservice.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records Management", description = "Endpoints for managing medical records")
public class MedicalRecordController {
    
    private final MedicalRecordService medicalRecordService;
    
    @PostMapping
    @Operation(summary = "Create new medical record")
    public ResponseEntity<MedicalRecordResponse> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse response = medicalRecordService.createRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get medical record by ID")
    public ResponseEntity<MedicalRecordResponse> getRecordById(@PathVariable String id) {
        MedicalRecordResponse response = medicalRecordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all medical records")
    public ResponseEntity<List<MedicalRecordResponse>> getAllRecords() {
        List<MedicalRecordResponse> records = medicalRecordService.getAllRecords();
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get medical records by patient ID")
    public ResponseEntity<List<MedicalRecordResponse>> getRecordsByPatient(@PathVariable String patientId) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByPatient(patientId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get medical records by doctor ID")
    public ResponseEntity<List<MedicalRecordResponse>> getRecordsByDoctor(@PathVariable String doctorId) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByDoctor(doctorId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/patient/{patientId}/type/{recordType}")
    @Operation(summary = "Get medical records by patient and type")
    public ResponseEntity<List<MedicalRecordResponse>> getRecordsByPatientAndType(
            @PathVariable String patientId,
            @PathVariable RecordType recordType) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByPatientAndType(patientId, recordType);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/patient/{patientId}/date-range")
    @Operation(summary = "Get medical records by patient and date range")
    public ResponseEntity<List<MedicalRecordResponse>> getRecordsByPatientAndDateRange(
            @PathVariable String patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByPatientAndDateRange(
                patientId, startDate, endDate);
        return ResponseEntity.ok(records);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update medical record")
    public ResponseEntity<MedicalRecordResponse> updateRecord(
            @PathVariable String id,
            @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse response = medicalRecordService.updateRecord(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medical record")
    public ResponseEntity<Void> deleteRecord(@PathVariable String id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
