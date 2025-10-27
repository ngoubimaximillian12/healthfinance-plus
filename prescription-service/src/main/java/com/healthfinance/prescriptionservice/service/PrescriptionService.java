package com.healthfinance.prescriptionservice.service;

import com.healthfinance.prescriptionservice.dto.PrescriptionRequest;
import com.healthfinance.prescriptionservice.model.Prescription;
import com.healthfinance.prescriptionservice.model.PrescriptionStatus;
import com.healthfinance.prescriptionservice.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {
    
    private final PrescriptionRepository prescriptionRepository;
    private final AutoDispenseService autoDispenseService;
    
    @Transactional
    public Prescription createPrescription(PrescriptionRequest request) {
        Prescription prescription = Prescription.builder()
                .prescriptionNumber(request.getPrescriptionNumber())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .medicationName(request.getMedicationName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .duration(request.getDuration())
                .quantity(request.getQuantity())
                .refillsAllowed(request.getRefillsAllowed())
                .instructions(request.getInstructions())
                .status(PrescriptionStatus.ACTIVE)
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .build();
        
        Prescription saved = prescriptionRepository.save(prescription);
        
        // Auto-dispense if approved
        if (saved.getStatus() == PrescriptionStatus.ACTIVE) {
            autoDispenseService.autoDispensePrescription(saved);
        }
        
        log.info("Prescription created: {}", saved.getId());
        return saved;
    }
    
    public Prescription getPrescriptionById(String id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }
    
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
    
    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }
    
    public List<Prescription> getPrescriptionsByDoctor(String doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
    
    @Transactional
    public Prescription updatePrescriptionStatus(String id, PrescriptionStatus status) {
        Prescription prescription = getPrescriptionById(id);
        prescription.setStatus(status);
        return prescriptionRepository.save(prescription);
    }
    
    @Transactional
    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }
}
