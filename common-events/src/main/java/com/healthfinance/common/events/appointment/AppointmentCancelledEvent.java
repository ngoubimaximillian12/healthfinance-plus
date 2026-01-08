package com.healthfinance.common.events.appointment;

import com.healthfinance.common.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Event published when an appointment is cancelled.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppointmentCancelledEvent extends BaseEvent {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String cancellationReason;
    private String cancelledBy; // PATIENT, DOCTOR, SYSTEM
    private Boolean rescheduleRequested;
}
