package com.healthfinance.billingservice.controller;

import com.healthfinance.billingservice.dto.PaymentRequest;
import com.healthfinance.billingservice.dto.PaymentResponse;
import com.healthfinance.billingservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Endpoints for managing payments")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping
    @Operation(summary = "Create new payment")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get payments by patient ID")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPatient(@PathVariable String patientId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByPatient(patientId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get payments by invoice ID")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByInvoice(@PathVariable String invoiceId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(payments);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
