package com.healthfinance.medicalrecordsservice.repository;

import com.healthfinance.medicalrecordsservice.model.MedicalRecord;
import com.healthfinance.medicalrecordsservice.model.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    
    List<MedicalRecord> findByPatientId(String patientId);
    
    List<MedicalRecord> findByDoctorId(String doctorId);
    
    List<MedicalRecord> findByAppointmentId(String appointmentId);
    
    List<MedicalRecord> findByPatientIdAndRecordType(String patientId, RecordType recordType);
    
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(String patientId);
    
    @Query("SELECT m FROM MedicalRecord m WHERE m.patientId = :patientId " +
           "AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByPatientIdAndDateRange(
            @Param("patientId") String patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    List<MedicalRecord> findByPatientIdAndIsConfidential(String patientId, Boolean isConfidential);
}
