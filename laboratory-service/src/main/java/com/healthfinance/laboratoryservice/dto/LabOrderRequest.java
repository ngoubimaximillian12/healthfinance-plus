package com.healthfinance.laboratoryservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderRequest {
    
    @NotNull(message = "Order number is required")
    private String orderNumber;
    
    @NotNull(message = "Patient ID is required")
    private String patientId;
    
    @NotNull(message = "Doctor ID is required")
    private String doctorId;
    
    private String appointmentId;
    
    @NotNull(message = "Test ID is required")
    private String testId;
    
    @NotNull(message = "Order date is required")
    private LocalDate orderDate;
    
    private LocalDate scheduledDate;
    private String clinicalNotes;
    private String urgencyLevel;
}
