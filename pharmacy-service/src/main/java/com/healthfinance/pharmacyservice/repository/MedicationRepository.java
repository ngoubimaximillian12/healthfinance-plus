package com.healthfinance.pharmacyservice.repository;

import com.healthfinance.pharmacyservice.model.Medication;
import com.healthfinance.pharmacyservice.model.MedicationCategory;
import com.healthfinance.pharmacyservice.model.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, String> {
    Optional<Medication> findByMedicationCode(String medicationCode);
    List<Medication> findByCategory(MedicationCategory category);
    List<Medication> findByStockStatus(StockStatus stockStatus);
    List<Medication> findByIsActive(Boolean isActive);
    List<Medication> findByQuantityInStockLessThanEqual(Integer threshold);
}
