package com.healthfinance.prescriptionservice.dto;

import com.healthfinance.prescriptionservice.model.MedicationFrequency;
import com.healthfinance.prescriptionservice.model.MedicationRoute;
import com.healthfinance.prescriptionservice.model.Prescription;
import com.healthfinance.prescriptionservice.model.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    
    private String id;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private String medicalRecordId;
    private String medicationName;
    private String medicationGenericName;
    private String medicationStrength;
    private String medicationForm;
    private MedicationRoute route;
    private String dosage;
    private MedicationFrequency frequency;
    private Integer durationDays;
    private Integer quantity;
    private Integer refillsAllowed;
    private Integer refillsRemaining;
    private LocalDate prescribedDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastFilledDate;
    private PrescriptionStatus status;
    private String instructions;
    private String warnings;
    private String sideEffects;
    private String interactions;
    private String notes;
    private String pharmacyId;
    private String pharmacyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static PrescriptionResponse fromPrescription(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatientId())
                .doctorId(prescription.getDoctorId())
                .appointmentId(prescription.getAppointmentId())
                .medicalRecordId(prescription.getMedicalRecordId())
                .medicationName(prescription.getMedicationName())
                .medicationGenericName(prescription.getMedicationGenericName())
                .medicationStrength(prescription.getMedicationStrength())
                .medicationForm(prescription.getMedicationForm())
                .route(prescription.getRoute())
                .dosage(prescription.getDosage())
                .frequency(prescription.getFrequency())
                .durationDays(prescription.getDurationDays())
                .quantity(prescription.getQuantity())
                .refillsAllowed(prescription.getRefillsAllowed())
                .refillsRemaining(prescription.getRefillsRemaining())
                .prescribedDate(prescription.getPrescribedDate())
                .startDate(prescription.getStartDate())
                .endDate(prescription.getEndDate())
                .lastFilledDate(prescription.getLastFilledDate())
                .status(prescription.getStatus())
                .instructions(prescription.getInstructions())
                .warnings(prescription.getWarnings())
                .sideEffects(prescription.getSideEffects())
                .interactions(prescription.getInteractions())
                .notes(prescription.getNotes())
                .pharmacyId(prescription.getPharmacyId())
                .pharmacyName(prescription.getPharmacyName())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .build();
    }
}
