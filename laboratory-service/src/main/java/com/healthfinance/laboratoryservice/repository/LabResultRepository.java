package com.healthfinance.laboratoryservice.repository;

import com.healthfinance.laboratoryservice.model.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, String> {
    List<LabResult> findByOrderId(String orderId);
}
