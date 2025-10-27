package com.healthfinance.laboratoryservice.dto;

import com.healthfinance.laboratoryservice.model.ResultStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultRequest {
    
    @NotNull(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Test parameter is required")
    private String testParameter;
    
    @NotNull(message = "Result value is required")
    private String resultValue;
    
    private String unit;
    private String referenceRange;
    private ResultStatus resultStatus;
    private String interpretation;
    private String notes;
}
