package com.healthfinance.laboratoryservice.model;

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
@Table(name = "lab_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String doctorId;
    
    private String appointmentId;
    
    @Column(nullable = false)
    private String testId;
    
    @Column(nullable = false)
    private LocalDate orderDate;
    
    private LocalDate scheduledDate;
    private LocalDate sampleCollectedDate;
    private LocalDate resultDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;
    
    @Column(length = 2000)
    private String clinicalNotes;
    
    @Column(length = 1000)
    private String urgencyLevel;
    
    private String labTechnicianId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
