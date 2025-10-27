package com.healthfinance.laboratoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String testCode;
    
    @Column(nullable = false)
    private String testName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestCategory category;
    
    @Column(length = 2000)
    private String description;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    private String sampleType;
    private Integer preparationTimeMinutes;
    private Integer processingTimeHours;
    
    @Column(length = 1000)
    private String preparationInstructions;
    
    private Boolean isActive;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
