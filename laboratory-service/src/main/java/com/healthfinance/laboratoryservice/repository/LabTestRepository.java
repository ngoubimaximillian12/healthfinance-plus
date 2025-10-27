package com.healthfinance.laboratoryservice.repository;

import com.healthfinance.laboratoryservice.model.LabTest;
import com.healthfinance.laboratoryservice.model.TestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, String> {
    Optional<LabTest> findByTestCode(String testCode);
    List<LabTest> findByCategory(TestCategory category);
    List<LabTest> findByIsActive(Boolean isActive);
}
