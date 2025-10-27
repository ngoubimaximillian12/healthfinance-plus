package com.healthfinance.medicalrecordsservice.dto;

import com.healthfinance.medicalrecordsservice.model.RecordType;
import com.healthfinance.medicalrecordsservice.model.Severity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    @NotNull(message = "Doctor ID is required")
    private String doctorId;
    
    private String appointmentId;
    
    @NotNull(message = "Record type is required")
    private RecordType recordType;
    
    @NotNull(message = "Record date is required")
    private LocalDate recordDate;
    
    @NotNull(message = "Title is required")
    private String title;
    
    private String description;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String medications;
    private Severity severity;
    
    // Vital Signs
    private Double bloodPressureSystolic;
    private Double bloodPressureDiastolic;
    private Double heartRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double oxygenSaturation;
    
    // Lab Results
    private String labTestName;
    private String labTestResult;
    private String labTestUnit;
    private String labTestReferenceRange;
    
    // Imaging
    private String imagingType;
    private String imagingFindings;
    private String imagingUrl;
    
    // Allergies
    private String allergen;
    private String allergyReaction;
    
    private String notes;
    private Boolean isConfidential;
}
