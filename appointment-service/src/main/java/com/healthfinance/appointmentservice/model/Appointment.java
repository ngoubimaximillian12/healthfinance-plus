package com.healthfinance.appointmentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private String doctorId;
    
    @Column(nullable = false)
    private LocalDate appointmentDate;
    
    @Column(nullable = false)
    private LocalTime appointmentTime;
    
    @Column(nullable = false)
    private Integer durationMinutes; // Duration in minutes
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;
    
    @Column(length = 1000)
    private String reason;
    
    @Column(length = 2000)
    private String notes;
    
    @Column(length = 2000)
    private String doctorNotes;
    
    private String cancellationReason;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
