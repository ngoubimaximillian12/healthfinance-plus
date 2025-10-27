package com.healthfinance.laboratoryservice.repository;

import com.healthfinance.laboratoryservice.model.LabOrder;
import com.healthfinance.laboratoryservice.model.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, String> {
    Optional<LabOrder> findByOrderNumber(String orderNumber);
    List<LabOrder> findByPatientId(String patientId);
    List<LabOrder> findByDoctorId(String doctorId);
    List<LabOrder> findByStatus(TestStatus status);
    List<LabOrder> findByPatientIdAndStatus(String patientId, TestStatus status);
}
