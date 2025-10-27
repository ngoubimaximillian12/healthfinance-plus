package com.healthfinance.insuranceservice.controller;

import com.healthfinance.insuranceservice.dto.InsurancePolicyRequest;
import com.healthfinance.insuranceservice.dto.InsurancePolicyResponse;
import com.healthfinance.insuranceservice.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance/policies")
@RequiredArgsConstructor
@Tag(name = "Insurance Policy Management", description = "Endpoints for managing insurance policies")
public class InsurancePolicyController {
    
    private final InsurancePolicyService policyService;
    
    @PostMapping
    @Operation(summary = "Create new insurance policy")
    public ResponseEntity<InsurancePolicyResponse> createPolicy(@Valid @RequestBody InsurancePolicyRequest request) {
        InsurancePolicyResponse response = policyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get policy by ID")
    public ResponseEntity<InsurancePolicyResponse> getPolicyById(@PathVariable String id) {
        InsurancePolicyResponse response = policyService.getPolicyById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all policies")
    public ResponseEntity<List<InsurancePolicyResponse>> getAllPolicies() {
        List<InsurancePolicyResponse> policies = policyService.getAllPolicies();
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get policies by user ID")
    public ResponseEntity<List<InsurancePolicyResponse>> getPoliciesByUser(@PathVariable String userId) {
        List<InsurancePolicyResponse> policies = policyService.getPoliciesByUser(userId);
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get active policies by user ID")
    public ResponseEntity<List<InsurancePolicyResponse>> getActivePoliciesByUser(@PathVariable String userId) {
        List<InsurancePolicyResponse> policies = policyService.getActivePoliciesByUser(userId);
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/user/{userId}/primary")
    @Operation(summary = "Get primary policy for user")
    public ResponseEntity<InsurancePolicyResponse> getPrimaryPolicy(@PathVariable String userId) {
        InsurancePolicyResponse response = policyService.getPrimaryPolicy(userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update policy")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(@PathVariable String id, 
                                                                @Valid @RequestBody InsurancePolicyRequest request) {
        InsurancePolicyResponse response = policyService.updatePolicy(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel policy")
    public ResponseEntity<Void> cancelPolicy(@PathVariable String id) {
        policyService.cancelPolicy(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete policy")
    public ResponseEntity<Void> deletePolicy(@PathVariable String id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
