package com.healthfinance.pharmacyservice.repository;

import com.healthfinance.pharmacyservice.model.DispenseOrder;
import com.healthfinance.pharmacyservice.model.DispenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispenseOrderRepository extends JpaRepository<DispenseOrder, String> {
    Optional<DispenseOrder> findByOrderNumber(String orderNumber);
    List<DispenseOrder> findByPatientId(String patientId);
    List<DispenseOrder> findByPrescriptionId(String prescriptionId);
    List<DispenseOrder> findByStatus(DispenseStatus status);
    List<DispenseOrder> findByPatientIdAndStatus(String patientId, DispenseStatus status);
}
