package com.healthfinance.pharmacyservice.service;

import com.healthfinance.pharmacyservice.dto.MedicationRequest;
import com.healthfinance.pharmacyservice.model.Medication;
import com.healthfinance.pharmacyservice.model.StockStatus;
import com.healthfinance.pharmacyservice.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationService {
    
    private final MedicationRepository medicationRepository;
    
    @Transactional
    public Medication createMedication(MedicationRequest request) {
        Medication medication = Medication.builder()
                .medicationCode(request.getMedicationCode())
                .name(request.getName())
                .genericName(request.getGenericName())
                .brandName(request.getBrandName())
                .category(request.getCategory())
                .description(request.getDescription())
                .manufacturer(request.getManufacturer())
                .dosageForm(request.getDosageForm())
                .strength(request.getStrength())
                .unitPrice(request.getUnitPrice())
                .quantityInStock(request.getQuantityInStock() != null ? request.getQuantityInStock() : 0)
                .reorderLevel(request.getReorderLevel())
                .ndc(request.getNdc())
                .expirationDate(request.getExpirationDate())
                .lotNumber(request.getLotNumber())
                .storageRequirements(request.getStorageRequirements())
                .sideEffects(request.getSideEffects())
                .contraindications(request.getContraindications())
                .requiresPrescription(request.getRequiresPrescription())
                .isControlled(request.getIsControlled())
                .controlledSubstanceSchedule(request.getControlledSubstanceSchedule())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        
        updateStockStatus(medication);
        return medicationRepository.save(medication);
    }
    
    public Medication getMedicationById(String id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));
    }
    
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
    
    public List<Medication> getActiveMedications() {
        return medicationRepository.findByIsActive(true);
    }
    
    public List<Medication> getLowStockMedications() {
        return medicationRepository.findByStockStatus(StockStatus.LOW_STOCK);
    }
    
    @Transactional
    public Medication updateStock(String id, Integer quantity) {
        Medication medication = getMedicationById(id);
        medication.setQuantityInStock(medication.getQuantityInStock() + quantity);
        updateStockStatus(medication);
        return medicationRepository.save(medication);
    }
    
    private void updateStockStatus(Medication medication) {
        if (medication.getQuantityInStock() == 0) {
            medication.setStockStatus(StockStatus.OUT_OF_STOCK);
        } else if (medication.getReorderLevel() != null && 
                   medication.getQuantityInStock() <= medication.getReorderLevel()) {
            medication.setStockStatus(StockStatus.LOW_STOCK);
        } else {
            medication.setStockStatus(StockStatus.IN_STOCK);
        }
    }
    
    @Transactional
    public void deleteMedication(String id) {
        medicationRepository.deleteById(id);
    }
}
