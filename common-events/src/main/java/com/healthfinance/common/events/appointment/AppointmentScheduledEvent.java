package com.healthfinance.common.events.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthfinance.common.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event published when an appointment is scheduled.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppointmentScheduledEvent extends BaseEvent {

    private String appointmentId;
    private String patientId;
    private String doctorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    private Integer durationMinutes;
    private String status;
    private String type;
    private String reason;
    private String notes;
}
