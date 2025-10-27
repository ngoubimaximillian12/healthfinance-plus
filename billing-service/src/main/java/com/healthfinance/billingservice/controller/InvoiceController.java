package com.healthfinance.billingservice.controller;

import com.healthfinance.billingservice.dto.InvoiceRequest;
import com.healthfinance.billingservice.dto.InvoiceResponse;
import com.healthfinance.billingservice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice Management", description = "Endpoints for managing invoices")
public class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    @PostMapping
    @Operation(summary = "Create new invoice")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable String id) {
        InvoiceResponse response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get invoices by patient ID")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPatient(@PathVariable String patientId) {
        List<InvoiceResponse> invoices = invoiceService.getInvoicesByPatient(patientId);
        return ResponseEntity.ok(invoices);
    }
    
    @GetMapping("/patient/{patientId}/pending")
    @Operation(summary = "Get pending invoices by patient ID")
    public ResponseEntity<List<InvoiceResponse>> getPendingInvoicesByPatient(@PathVariable String patientId) {
        List<InvoiceResponse> invoices = invoiceService.getPendingInvoicesByPatient(patientId);
        return ResponseEntity.ok(invoices);
    }
    
    @GetMapping("/patient/{patientId}/overdue")
    @Operation(summary = "Get overdue invoices by patient ID")
    public ResponseEntity<List<InvoiceResponse>> getOverdueInvoicesByPatient(@PathVariable String patientId) {
        List<InvoiceResponse> invoices = invoiceService.getOverdueInvoicesByPatient(patientId);
        return ResponseEntity.ok(invoices);
    }
    
    @PostMapping("/{id}/mark-paid")
    @Operation(summary = "Mark invoice as paid")
    public ResponseEntity<InvoiceResponse> markAsPaid(@PathVariable String id) {
        InvoiceResponse response = invoiceService.markAsPaid(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<Void> cancelInvoice(@PathVariable String id) {
        invoiceService.cancelInvoice(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
