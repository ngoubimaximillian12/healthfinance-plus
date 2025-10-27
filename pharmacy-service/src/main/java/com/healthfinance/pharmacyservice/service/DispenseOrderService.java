package com.healthfinance.pharmacyservice.service;

import com.healthfinance.pharmacyservice.dto.DispenseOrderRequest;
import com.healthfinance.pharmacyservice.model.DispenseOrder;
import com.healthfinance.pharmacyservice.model.DispenseStatus;
import com.healthfinance.pharmacyservice.repository.DispenseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispenseOrderService {
    
    private final DispenseOrderRepository dispenseOrderRepository;
    private final MedicationService medicationService;
    
    @Transactional
    public DispenseOrder createDispenseOrder(DispenseOrderRequest request) {
        DispenseOrder order = DispenseOrder.builder()
                .orderNumber(request.getOrderNumber())
                .prescriptionId(request.getPrescriptionId())
                .patientId(request.getPatientId())
                .medicationId(request.getMedicationId())
                .quantityDispensed(request.getQuantityDispensed())
                .refillsRemaining(request.getRefillsRemaining())
                .dispenseDate(request.getDispenseDate())
                .status(DispenseStatus.PENDING)
                .pharmacistId(request.getPharmacistId())
                .pharmacistName(request.getPharmacistName())
                .totalCost(request.getTotalCost())
                .insuranceCoverage(request.getInsuranceCoverage())
                .patientCopay(request.getPatientCopay())
                .dispensingInstructions(request.getDispensingInstructions())
                .counselingNotes(request.getCounselingNotes())
                .build();
        
        // Update medication stock
        medicationService.updateStock(request.getMedicationId(), -request.getQuantityDispensed());
        
        return dispenseOrderRepository.save(order);
    }
    
    public DispenseOrder getDispenseOrderById(String id) {
        return dispenseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispense order not found"));
    }
    
    public List<DispenseOrder> getAllDispenseOrders() {
        return dispenseOrderRepository.findAll();
    }
    
    public List<DispenseOrder> getDispenseOrdersByPatient(String patientId) {
        return dispenseOrderRepository.findByPatientId(patientId);
    }
    
    @Transactional
    public DispenseOrder updateOrderStatus(String id, DispenseStatus status) {
        DispenseOrder order = getDispenseOrderById(id);
        order.setStatus(status);
        
        if (status == DispenseStatus.DISPENSED) {
            order.setPickupDateTime(LocalDateTime.now());
        }
        
        return dispenseOrderRepository.save(order);
    }
    
    @Transactional
    public void deleteDispenseOrder(String id) {
        dispenseOrderRepository.deleteById(id);
    }
}
