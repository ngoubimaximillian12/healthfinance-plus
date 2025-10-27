package com.healthfinance.appointmentservice.repository;

import com.healthfinance.appointmentservice.model.Appointment;
import com.healthfinance.appointmentservice.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    List<Appointment> findByPatientId(String patientId);
    
    List<Appointment> findByDoctorId(String doctorId);
    
    List<Appointment> findByPatientIdAndStatus(String patientId, AppointmentStatus status);
    
    List<Appointment> findByDoctorIdAndStatus(String doctorId, AppointmentStatus status);
    
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    
    List<Appointment> findByDoctorIdAndAppointmentDate(String doctorId, LocalDate appointmentDate);
    
    List<Appointment> findByPatientIdAndAppointmentDate(String patientId, LocalDate appointmentDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.appointmentTime = :time " +
           "AND a.status NOT IN ('CANCELLED', 'COMPLETED')")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") String doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
