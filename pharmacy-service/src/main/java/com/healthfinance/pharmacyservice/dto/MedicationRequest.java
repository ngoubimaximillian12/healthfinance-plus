package com.healthfinance.pharmacyservice.dto;

import com.healthfinance.pharmacyservice.model.MedicationCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequest {
    
    @NotNull(message = "Medication code is required")
    private String medicationCode;
    
    @NotNull(message = "Name is required")
    private String name;
    
    private String genericName;
    private String brandName;
    
    @NotNull(message = "Category is required")
    private MedicationCategory category;
    
    private String description;
    private String manufacturer;
    private String dosageForm;
    private String strength;
    private BigDecimal unitPrice;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private String ndc;
    private LocalDate expirationDate;
    private String lotNumber;
    private String storageRequirements;
    private String sideEffects;
    private String contraindications;
    private Boolean requiresPrescription;
    private Boolean isControlled;
    private String controlledSubstanceSchedule;
    private Boolean isActive;
}
