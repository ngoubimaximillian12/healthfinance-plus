package com.healthfinance.appointmentservice.service;

import com.healthfinance.appointmentservice.dto.AppointmentRequest;
import com.healthfinance.appointmentservice.dto.AppointmentResponse;
import com.healthfinance.appointmentservice.dto.AppointmentUpdateRequest;
import com.healthfinance.appointmentservice.model.Appointment;
import com.healthfinance.appointmentservice.model.AppointmentStatus;
import com.healthfinance.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);
        return AppointmentResponse.fromAppointment(appointment);
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return AppointmentResponse.fromAppointment(appointment);
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponse::fromAppointment)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentResponse::fromAppointment)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentResponse::fromAppointment)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(AppointmentResponse::fromAppointment)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctorAndDate(String doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date).stream()
                .map(AppointmentResponse::fromAppointment)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse updateAppointment(String id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(request.getAppointmentDate());
        }
        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }
        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }
        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        
        appointment = appointmentRepository.save(appointment);
        return AppointmentResponse.fromAppointment(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(String id, AppointmentStatus status, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setStatus(status);
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        
        appointment = appointmentRepository.save(appointment);
        return AppointmentResponse.fromAppointment(appointment);
    }

    @Transactional
    public void cancelAppointment(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }
}
