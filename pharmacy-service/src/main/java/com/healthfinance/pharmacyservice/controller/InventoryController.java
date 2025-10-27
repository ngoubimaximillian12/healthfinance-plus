package com.healthfinance.pharmacyservice.controller;

import com.healthfinance.pharmacyservice.dto.InventoryRequest;
import com.healthfinance.pharmacyservice.model.Inventory;
import com.healthfinance.pharmacyservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @PostMapping
    @Operation(summary = "Record inventory transaction")
    public ResponseEntity<Inventory> recordInventoryTransaction(@Valid @RequestBody InventoryRequest request) {
        Inventory inventory = inventoryService.recordInventoryTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }
    
    @GetMapping("/medication/{medicationId}")
    @Operation(summary = "Get inventory by medication")
    public ResponseEntity<List<Inventory>> getInventoryByMedication(@PathVariable String medicationId) {
        List<Inventory> inventory = inventoryService.getInventoryByMedication(medicationId);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping
    @Operation(summary = "Get all inventory transactions")
    public ResponseEntity<List<Inventory>> getAllInventoryTransactions() {
        List<Inventory> inventory = inventoryService.getAllInventoryTransactions();
        return ResponseEntity.ok(inventory);
    }
}
