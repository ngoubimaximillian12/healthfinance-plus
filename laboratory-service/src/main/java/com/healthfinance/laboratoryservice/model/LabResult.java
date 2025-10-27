package com.healthfinance.laboratoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String orderId;
    
    @Column(nullable = false)
    private String testParameter;
    
    @Column(nullable = false)
    private String resultValue;
    
    private String unit;
    private String referenceRange;
    
    @Enumerated(EnumType.STRING)
    private ResultStatus resultStatus;
    
    @Column(length = 2000)
    private String interpretation;
    
    @Column(length = 1000)
    private String notes;
    
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
