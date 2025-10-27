package com.healthfinance.pharmacyservice.service;

import com.healthfinance.pharmacyservice.dto.InventoryRequest;
import com.healthfinance.pharmacyservice.model.Inventory;
import com.healthfinance.pharmacyservice.model.Medication;
import com.healthfinance.pharmacyservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final MedicationService medicationService;
    
    @Transactional
    public Inventory recordInventoryTransaction(InventoryRequest request) {
        Medication medication = medicationService.getMedicationById(request.getMedicationId());
        
        Integer balanceAfter = medication.getQuantityInStock();
        
        Inventory inventory = Inventory.builder()
                .medicationId(request.getMedicationId())
                .transactionType(request.getTransactionType())
                .quantity(request.getQuantity())
                .balanceAfter(balanceAfter)
                .referenceNumber(request.getReferenceNumber())
                .notes(request.getNotes())
                .performedBy(request.getPerformedBy())
                .build();
        
        return inventoryRepository.save(inventory);
    }
    
    public List<Inventory> getInventoryByMedication(String medicationId) {
        return inventoryRepository.findByMedicationIdOrderByCreatedAtDesc(medicationId);
    }
    
    public List<Inventory> getAllInventoryTransactions() {
        return inventoryRepository.findAll();
    }
}
