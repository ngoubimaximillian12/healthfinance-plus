package com.healthfinance.prescriptionservice.repository;

import com.healthfinance.prescriptionservice.model.Prescription;
import com.healthfinance.prescriptionservice.model.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, String> {
    
    List<Prescription> findByPatientId(String patientId);
    
    List<Prescription> findByDoctorId(String doctorId);
    
    List<Prescription> findByPatientIdAndStatus(String patientId, PrescriptionStatus status);
    
    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(String patientId);
    
    List<Prescription> findByStatus(PrescriptionStatus status);
    
    @Query("SELECT p FROM Prescription p WHERE p.endDate < :date AND p.status = 'ACTIVE'")
    List<Prescription> findExpiredPrescriptions(@Param("date") LocalDate date);
    
    List<Prescription> findByPatientIdAndMedicationNameContainingIgnoreCase(String patientId, String medicationName);
}
