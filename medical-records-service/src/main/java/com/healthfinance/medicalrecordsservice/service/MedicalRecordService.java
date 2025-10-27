package com.healthfinance.medicalrecordsservice.service;

import com.healthfinance.medicalrecordsservice.dto.MedicalRecordRequest;
import com.healthfinance.medicalrecordsservice.dto.MedicalRecordResponse;
import com.healthfinance.medicalrecordsservice.model.MedicalRecord;
import com.healthfinance.medicalrecordsservice.model.RecordType;
import com.healthfinance.medicalrecordsservice.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {
    
    private final MedicalRecordRepository medicalRecordRepository;
    
    @Transactional
    public MedicalRecordResponse createRecord(MedicalRecordRequest request) {
        log.info("Creating medical record for patient: {}", request.getPatientId());
        
        MedicalRecord record = MedicalRecord.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentId(request.getAppointmentId())
                .recordType(request.getRecordType())
                .recordDate(request.getRecordDate())
                .title(request.getTitle())
                .description(request.getDescription())
                .diagnosis(request.getDiagnosis())
                .symptoms(request.getSymptoms())
                .treatment(request.getTreatment())
                .medications(request.getMedications())
                .severity(request.getSeverity())
                .bloodPressureSystolic(request.getBloodPressureSystolic())
                .bloodPressureDiastolic(request.getBloodPressureDiastolic())
                .heartRate(request.getHeartRate())
                .temperature(request.getTemperature())
                .weight(request.getWeight())
                .height(request.getHeight())
                .oxygenSaturation(request.getOxygenSaturation())
                .labTestName(request.getLabTestName())
                .labTestResult(request.getLabTestResult())
                .labTestUnit(request.getLabTestUnit())
                .labTestReferenceRange(request.getLabTestReferenceRange())
                .imagingType(request.getImagingType())
                .imagingFindings(request.getImagingFindings())
                .imagingUrl(request.getImagingUrl())
                .allergen(request.getAllergen())
                .allergyReaction(request.getAllergyReaction())
                .notes(request.getNotes())
                .isConfidential(request.getIsConfidential() != null ? request.getIsConfidential() : false)
                .build();
        
        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("Medical record created with ID: {}", saved.getId());
        
        return MedicalRecordResponse.fromMedicalRecord(saved);
    }
    
    public MedicalRecordResponse getRecordById(String id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        return MedicalRecordResponse.fromMedicalRecord(record);
    }
    
    public List<MedicalRecordResponse> getAllRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(MedicalRecordResponse::fromMedicalRecord)
                .collect(Collectors.toList());
    }
    
    public List<MedicalRecordResponse> getRecordsByPatient(String patientId) {
        return medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId).stream()
                .map(MedicalRecordResponse::fromMedicalRecord)
                .collect(Collectors.toList());
    }
    
    public List<MedicalRecordResponse> getRecordsByDoctor(String doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(MedicalRecordResponse::fromMedicalRecord)
                .collect(Collectors.toList());
    }
    
    public List<MedicalRecordResponse> getRecordsByPatientAndType(String patientId, RecordType recordType) {
        return medicalRecordRepository.findByPatientIdAndRecordType(patientId, recordType).stream()
                .map(MedicalRecordResponse::fromMedicalRecord)
                .collect(Collectors.toList());
    }
    
    public List<MedicalRecordResponse> getRecordsByPatientAndDateRange(
            String patientId, LocalDate startDate, LocalDate endDate) {
        return medicalRecordRepository.findByPatientIdAndDateRange(patientId, startDate, endDate).stream()
                .map(MedicalRecordResponse::fromMedicalRecord)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public MedicalRecordResponse updateRecord(String id, MedicalRecordRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        
        if (request.getTitle() != null) record.setTitle(request.getTitle());
        if (request.getDescription() != null) record.setDescription(request.getDescription());
        if (request.getDiagnosis() != null) record.setDiagnosis(request.getDiagnosis());
        if (request.getSymptoms() != null) record.setSymptoms(request.getSymptoms());
        if (request.getTreatment() != null) record.setTreatment(request.getTreatment());
        if (request.getMedications() != null) record.setMedications(request.getMedications());
        if (request.getSeverity() != null) record.setSeverity(request.getSeverity());
        if (request.getNotes() != null) record.setNotes(request.getNotes());
        
        MedicalRecord updated = medicalRecordRepository.save(record);
        return MedicalRecordResponse.fromMedicalRecord(updated);
    }
    
    @Transactional
    public void deleteRecord(String id) {
        medicalRecordRepository.deleteById(id);
    }
}
