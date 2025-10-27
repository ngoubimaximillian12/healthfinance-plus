package com.healthfinance.appointmentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @NotNull
    private String patientId;
    
    @NotNull
    private String doctorId;
    
    @NotNull
    private LocalDate appointmentDate;
    
    @NotNull
    private LocalTime appointmentTime;
    
    private String reason;
    private String notes;
}
