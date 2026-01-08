package com.healthfinance.common.events.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthfinance.common.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Event published when an appointment is completed.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppointmentCompletedEvent extends BaseEvent {

    private String appointmentId;
    private String patientId;
    private String doctorId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completionTime;

    private List<String> diagnoses;
    private List<String> proceduresConducted;
    private Map<String, Object> vitalsRecorded;
    private String doctorNotes;
    private Boolean followUpRequired;
    private Integer followUpDays;
}
