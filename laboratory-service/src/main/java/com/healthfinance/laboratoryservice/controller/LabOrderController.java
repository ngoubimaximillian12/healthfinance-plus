package com.healthfinance.laboratoryservice.controller;

import com.healthfinance.laboratoryservice.dto.LabOrderRequest;
import com.healthfinance.laboratoryservice.model.LabOrder;
import com.healthfinance.laboratoryservice.model.TestStatus;
import com.healthfinance.laboratoryservice.service.LabOrderService;
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
@RequestMapping("/api/lab/orders")
@RequiredArgsConstructor
@Tag(name = "Lab Order Management")
public class LabOrderController {
    
    private final LabOrderService labOrderService;
    
    @PostMapping
    @Operation(summary = "Create lab order")
    public ResponseEntity<LabOrder> createLabOrder(@Valid @RequestBody LabOrderRequest request) {
        LabOrder labOrder = labOrderService.createLabOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(labOrder);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get lab order by ID")
    public ResponseEntity<LabOrder> getLabOrderById(@PathVariable String id) {
        LabOrder labOrder = labOrderService.getLabOrderById(id);
        return ResponseEntity.ok(labOrder);
    }
    
    @GetMapping
    @Operation(summary = "Get all lab orders")
    public ResponseEntity<List<LabOrder>> getAllLabOrders() {
        List<LabOrder> labOrders = labOrderService.getAllLabOrders();
        return ResponseEntity.ok(labOrders);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get lab orders by patient")
    public ResponseEntity<List<LabOrder>> getLabOrdersByPatient(@PathVariable String patientId) {
        List<LabOrder> labOrders = labOrderService.getLabOrdersByPatient(patientId);
        return ResponseEntity.ok(labOrders);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<LabOrder> updateOrderStatus(@PathVariable String id, 
                                                       @RequestBody Map<String, String> body) {
        TestStatus status = TestStatus.valueOf(body.get("status"));
        LabOrder updated = labOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lab order")
    public ResponseEntity<Void> deleteLabOrder(@PathVariable String id) {
        labOrderService.deleteLabOrder(id);
        return ResponseEntity.noContent().build();
    }
}
