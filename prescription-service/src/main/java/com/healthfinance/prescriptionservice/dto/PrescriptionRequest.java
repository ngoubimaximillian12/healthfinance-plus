package com.healthfinance.prescriptionservice.dto;

import com.healthfinance.prescriptionservice.model.MedicationFrequency;
import com.healthfinance.prescriptionservice.model.MedicationRoute;
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
public class PrescriptionRequest {
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    @NotNull(message = "Doctor ID is required")
    private String doctorId;
    
    private String appointmentId;
    private String medicalRecordId;
    
    @NotNull(message = "Medication name is required")
    private String medicationName;
    
    private String medicationGenericName;
    private String medicationStrength;
    private String medicationForm;
    
    @NotNull(message = "Route is required")
    private MedicationRoute route;
    
    @NotNull(message = "Dosage is required")
    private String dosage;
    
    @NotNull(message = "Frequency is required")
    private MedicationFrequency frequency;
    
    @NotNull(message = "Duration is required")
    private Integer durationDays;
    
    private Integer quantity;
    private Integer refillsAllowed;
    
    @NotNull(message = "Prescribed date is required")
    private LocalDate prescribedDate;
    
    private LocalDate startDate;
    private String instructions;
    private String warnings;
    private String sideEffects;
    private String interactions;
    private String notes;
    private String pharmacyId;
    private String pharmacyName;
}
