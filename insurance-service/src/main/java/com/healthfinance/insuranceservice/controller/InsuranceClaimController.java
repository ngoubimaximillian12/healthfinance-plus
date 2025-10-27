package com.healthfinance.insuranceservice.controller;

import com.healthfinance.insuranceservice.dto.InsuranceClaimRequest;
import com.healthfinance.insuranceservice.dto.InsuranceClaimResponse;
import com.healthfinance.insuranceservice.model.ClaimStatus;
import com.healthfinance.insuranceservice.service.InsuranceClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/insurance/claims")
@RequiredArgsConstructor
@Tag(name = "Insurance Claim Management", description = "Endpoints for managing insurance claims")
public class InsuranceClaimController {
    
    private final InsuranceClaimService claimService;
    
    @PostMapping
    @Operation(summary = "Create new insurance claim")
    public ResponseEntity<InsuranceClaimResponse> createClaim(@Valid @RequestBody InsuranceClaimRequest request) {
        InsuranceClaimResponse response = claimService.createClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get claim by ID")
    public ResponseEntity<InsuranceClaimResponse> getClaimById(@PathVariable String id) {
        InsuranceClaimResponse response = claimService.getClaimById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all claims")
    public ResponseEntity<List<InsuranceClaimResponse>> getAllClaims() {
        List<InsuranceClaimResponse> claims = claimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get claims by user ID")
    public ResponseEntity<List<InsuranceClaimResponse>> getClaimsByUser(@PathVariable String userId) {
        List<InsuranceClaimResponse> claims = claimService.getClaimsByUser(userId);
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/policy/{policyId}")
    @Operation(summary = "Get claims by policy ID")
    public ResponseEntity<List<InsuranceClaimResponse>> getClaimsByPolicy(@PathVariable String policyId) {
        List<InsuranceClaimResponse> claims = claimService.getClaimsByPolicy(policyId);
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get claims by user ID and status")
    public ResponseEntity<List<InsuranceClaimResponse>> getClaimsByStatus(@PathVariable String userId, 
                                                                           @PathVariable ClaimStatus status) {
        List<InsuranceClaimResponse> claims = claimService.getClaimsByStatus(userId, status);
        return ResponseEntity.ok(claims);
    }
    
    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve claim")
    public ResponseEntity<InsuranceClaimResponse> approveClaim(@PathVariable String id, 
                                                               @RequestBody Map<String, BigDecimal> amounts) {
        InsuranceClaimResponse response = claimService.approveClaim(
                id, 
                amounts.get("approvedAmount"), 
                amounts.get("insurancePayment"), 
                amounts.get("patientResponsibility")
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/deny")
    @Operation(summary = "Deny claim")
    public ResponseEntity<InsuranceClaimResponse> denyClaim(@PathVariable String id, 
                                                            @RequestBody Map<String, String> body) {
        InsuranceClaimResponse response = claimService.denyClaim(id, body.get("denialReason"));
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete claim")
    public ResponseEntity<Void> deleteClaim(@PathVariable String id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
