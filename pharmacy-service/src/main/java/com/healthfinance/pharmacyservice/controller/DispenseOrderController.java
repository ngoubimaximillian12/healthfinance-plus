package com.healthfinance.pharmacyservice.controller;

import com.healthfinance.pharmacyservice.dto.DispenseOrderRequest;
import com.healthfinance.pharmacyservice.model.DispenseOrder;
import com.healthfinance.pharmacyservice.model.DispenseStatus;
import com.healthfinance.pharmacyservice.service.DispenseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy/dispense")
@RequiredArgsConstructor
@Tag(name = "Dispense Order Management")
public class DispenseOrderController {
    
    private final DispenseOrderService dispenseOrderService;
    
    @PostMapping
    @Operation(summary = "Create dispense order")
    public ResponseEntity<DispenseOrder> createDispenseOrder(@Valid @RequestBody DispenseOrderRequest request) {
        DispenseOrder order = dispenseOrderService.createDispenseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get dispense order by ID")
    public ResponseEntity<DispenseOrder> getDispenseOrderById(@PathVariable String id) {
        DispenseOrder order = dispenseOrderService.getDispenseOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    @Operation(summary = "Get all dispense orders")
    public ResponseEntity<List<DispenseOrder>> getAllDispenseOrders() {
        List<DispenseOrder> orders = dispenseOrderService.getAllDispenseOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get dispense orders by patient")
    public ResponseEntity<List<DispenseOrder>> getDispenseOrdersByPatient(@PathVariable String patientId) {
        List<DispenseOrder> orders = dispenseOrderService.getDispenseOrdersByPatient(patientId);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<DispenseOrder> updateOrderStatus(@PathVariable String id, 
                                                            @RequestBody Map<String, String> body) {
        DispenseStatus status = DispenseStatus.valueOf(body.get("status"));
        DispenseOrder updated = dispenseOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dispense order")
    public ResponseEntity<Void> deleteDispenseOrder(@PathVariable String id) {
        dispenseOrderService.deleteDispenseOrder(id);
        return ResponseEntity.noContent().build();
    }
}
