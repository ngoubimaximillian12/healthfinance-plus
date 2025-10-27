package com.healthfinance.laboratoryservice.controller;

import com.healthfinance.laboratoryservice.dto.LabTestRequest;
import com.healthfinance.laboratoryservice.model.LabTest;
import com.healthfinance.laboratoryservice.service.LabTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab/tests")
@RequiredArgsConstructor
@Tag(name = "Lab Test Management")
public class LabTestController {
    
    private final LabTestService labTestService;
    
    @PostMapping
    @Operation(summary = "Create lab test")
    public ResponseEntity<LabTest> createLabTest(@Valid @RequestBody LabTestRequest request) {
        LabTest labTest = labTestService.createLabTest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(labTest);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get lab test by ID")
    public ResponseEntity<LabTest> getLabTestById(@PathVariable String id) {
        LabTest labTest = labTestService.getLabTestById(id);
        return ResponseEntity.ok(labTest);
    }
    
    @GetMapping
    @Operation(summary = "Get all lab tests")
    public ResponseEntity<List<LabTest>> getAllLabTests() {
        List<LabTest> labTests = labTestService.getAllLabTests();
        return ResponseEntity.ok(labTests);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active lab tests")
    public ResponseEntity<List<LabTest>> getActiveLabTests() {
        List<LabTest> labTests = labTestService.getActiveLabTests();
        return ResponseEntity.ok(labTests);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lab test")
    public ResponseEntity<Void> deleteLabTest(@PathVariable String id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }
}
