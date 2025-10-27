package com.healthfinance.medicalrecordsservice.model;

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
@Table(name = "medical_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String doctorId;
    
    private String appointmentId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType recordType;
    
    @Column(nullable = false)
    private LocalDate recordDate;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String description;
    
    @Column(length = 5000)
    private String diagnosis;
    
    @Column(length = 2000)
    private String symptoms;
    
    @Column(length = 2000)
    private String treatment;
    
    @Column(length = 1000)
    private String medications;
    
    @Enumerated(EnumType.STRING)
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
    
    @Column(length = 2000)
    private String notes;
    
    private Boolean isConfidential;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
