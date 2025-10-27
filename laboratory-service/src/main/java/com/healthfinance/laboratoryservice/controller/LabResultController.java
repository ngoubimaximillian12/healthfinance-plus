package com.healthfinance.laboratoryservice.controller;

import com.healthfinance.laboratoryservice.dto.LabResultRequest;
import com.healthfinance.laboratoryservice.model.LabResult;
import com.healthfinance.laboratoryservice.service.LabResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/results")
@RequiredArgsConstructor
@Tag(name = "Lab Result Management")
public class LabResultController {
    
    private final LabResultService labResultService;
    
    @PostMapping
    @Operation(summary = "Create lab result")
    public ResponseEntity<LabResult> createLabResult(@Valid @RequestBody LabResultRequest request) {
        LabResult labResult = labResultService.createLabResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(labResult);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get lab result by ID")
    public ResponseEntity<LabResult> getLabResultById(@PathVariable String id) {
        LabResult labResult = labResultService.getLabResultById(id);
        return ResponseEntity.ok(labResult);
    }
    
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get results by order")
    public ResponseEntity<List<LabResult>> getResultsByOrder(@PathVariable String orderId) {
        List<LabResult> labResults = labResultService.getResultsByOrder(orderId);
        return ResponseEntity.ok(labResults);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lab result")
    public ResponseEntity<Void> deleteLabResult(@PathVariable String id) {
        labResultService.deleteLabResult(id);
        return ResponseEntity.noContent().build();
    }
}
