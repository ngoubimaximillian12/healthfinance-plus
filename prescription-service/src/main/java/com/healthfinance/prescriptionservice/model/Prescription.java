package com.healthfinance.prescriptionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String doctorId;
    
    private String appointmentId;
    private String medicalRecordId;
    
    @Column(nullable = false)
    private String medicationName;
    
    private String medicationGenericName;
    private String medicationStrength;
    private String medicationForm;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationRoute route;
    
    @Column(nullable = false)
    private String dosage;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationFrequency frequency;
    
    @Column(nullable = false)
    private Integer durationDays;
    
    private Integer quantity;
    private Integer refillsAllowed;
    private Integer refillsRemaining;
    
    @Column(nullable = false)
    private LocalDate prescribedDate;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastFilledDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;
    
    @Column(length = 2000)
    private String instructions;
    
    @Column(length = 1000)
    private String warnings;
    
    @Column(length = 1000)
    private String sideEffects;
    
    @Column(length = 1000)
    private String interactions;
    
    @Column(length = 2000)
    private String notes;
    
    private String pharmacyId;
    private String pharmacyName;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
