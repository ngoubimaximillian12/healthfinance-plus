package com.healthfinance.pharmacyservice.model;

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
@Table(name = "medications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String medicationCode;
    
    @Column(nullable = false)
    private String name;
    
    private String genericName;
    private String brandName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationCategory category;
    
    @Column(length = 2000)
    private String description;
    
    private String manufacturer;
    private String dosageForm;
    private String strength;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    private Integer quantityInStock;
    private Integer reorderLevel;
    
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    
    private String ndc;
    private LocalDate expirationDate;
    private String lotNumber;
    
    @Column(length = 2000)
    private String storageRequirements;
    
    @Column(length = 2000)
    private String sideEffects;
    
    @Column(length = 2000)
    private String contraindications;
    
    private Boolean requiresPrescription;
    private Boolean isControlled;
    private String controlledSubstanceSchedule;
    
    private Boolean isActive;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
