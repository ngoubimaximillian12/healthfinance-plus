package com.healthfinance.laboratoryservice.service;

import com.healthfinance.laboratoryservice.dto.LabOrderRequest;
import com.healthfinance.laboratoryservice.model.LabOrder;
import com.healthfinance.laboratoryservice.model.TestStatus;
import com.healthfinance.laboratoryservice.repository.LabOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabOrderService {
    
    private final LabOrderRepository labOrderRepository;
    
    @Transactional
    public LabOrder createLabOrder(LabOrderRequest request) {
        LabOrder labOrder = LabOrder.builder()
                .orderNumber(request.getOrderNumber())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentId(request.getAppointmentId())
                .testId(request.getTestId())
                .orderDate(request.getOrderDate())
                .scheduledDate(request.getScheduledDate())
                .status(TestStatus.ORDERED)
                .clinicalNotes(request.getClinicalNotes())
                .urgencyLevel(request.getUrgencyLevel())
                .build();
        
        return labOrderRepository.save(labOrder);
    }
    
    public LabOrder getLabOrderById(String id) {
        return labOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab order not found"));
    }
    
    public List<LabOrder> getAllLabOrders() {
        return labOrderRepository.findAll();
    }
    
    public List<LabOrder> getLabOrdersByPatient(String patientId) {
        return labOrderRepository.findByPatientId(patientId);
    }
    
    @Transactional
    public LabOrder updateOrderStatus(String id, TestStatus status) {
        LabOrder order = getLabOrderById(id);
        order.setStatus(status);
        
        if (status == TestStatus.SAMPLE_COLLECTED) {
            order.setSampleCollectedDate(LocalDate.now());
        } else if (status == TestStatus.COMPLETED) {
            order.setResultDate(LocalDate.now());
        }
        
        return labOrderRepository.save(order);
    }
    
    @Transactional
    public void deleteLabOrder(String id) {
        labOrderRepository.deleteById(id);
    }
}
