package com.healthfinance.laboratoryservice.dto;

import com.healthfinance.laboratoryservice.model.TestCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestRequest {
    
    @NotNull(message = "Test code is required")
    private String testCode;
    
    @NotNull(message = "Test name is required")
    private String testName;
    
    @NotNull(message = "Category is required")
    private TestCategory category;
    
    private String description;
    private BigDecimal price;
    private String sampleType;
    private Integer preparationTimeMinutes;
    private Integer processingTimeHours;
    private String preparationInstructions;
    private Boolean isActive;
}
