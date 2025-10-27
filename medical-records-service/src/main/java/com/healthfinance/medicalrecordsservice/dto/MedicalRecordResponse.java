package com.healthfinance.medicalrecordsservice.dto;

import com.healthfinance.medicalrecordsservice.model.MedicalRecord;
import com.healthfinance.medicalrecordsservice.model.RecordType;
import com.healthfinance.medicalrecordsservice.model.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    
    private String id;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private RecordType recordType;
    private LocalDate recordDate;
    private String title;
    private String description;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String medications;
    private Severity severity;
    private Double bloodPressureSystolic;
    private Double bloodPressureDiastolic;
    private Double heartRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double oxygenSaturation;
    private String labTestName;
    private String labTestResult;
    private String labTestUnit;
    private String labTestReferenceRange;
    private String imagingType;
    private String imagingFindings;
    private String imagingUrl;
    private String allergen;
    private String allergyReaction;
    private String notes;
    private Boolean isConfidential;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static MedicalRecordResponse fromMedicalRecord(MedicalRecord record) {
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .doctorId(record.getDoctorId())
                .appointmentId(record.getAppointmentId())
                .recordType(record.getRecordType())
                .recordDate(record.getRecordDate())
                .title(record.getTitle())
                .description(record.getDescription())
                .diagnosis(record.getDiagnosis())
                .symptoms(record.getSymptoms())
                .treatment(record.getTreatment())
                .medications(record.getMedications())
                .severity(record.getSeverity())
                .bloodPressureSystolic(record.getBloodPressureSystolic())
                .bloodPressureDiastolic(record.getBloodPressureDiastolic())
                .heartRate(record.getHeartRate())
                .temperature(record.getTemperature())
                .weight(record.getWeight())
                .height(record.getHeight())
                .oxygenSaturation(record.getOxygenSaturation())
                .labTestName(record.getLabTestName())
                .labTestResult(record.getLabTestResult())
                .labTestUnit(record.getLabTestUnit())
                .labTestReferenceRange(record.getLabTestReferenceRange())
                .imagingType(record.getImagingType())
                .imagingFindings(record.getImagingFindings())
                .imagingUrl(record.getImagingUrl())
                .allergen(record.getAllergen())
                .allergyReaction(record.getAllergyReaction())
                .notes(record.getNotes())
                .isConfidential(record.getIsConfidential())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
