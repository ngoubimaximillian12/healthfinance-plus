package com.healthfinance.billingservice.service;

import com.healthfinance.billingservice.dto.InvoiceRequest;
import com.healthfinance.billingservice.model.Invoice;
import com.healthfinance.billingservice.model.InvoiceStatus;
import com.healthfinance.billingservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    
    private final InvoiceRepository invoiceRepository;
    private final AutoClaimService autoClaimService;
    
    @Transactional
    public Invoice createInvoice(InvoiceRequest request) {
        Invoice invoice = Invoice.builder()
                .invoiceNumber(request.getInvoiceNumber())
                .patientId(request.getPatientId())
                .serviceDate(request.getServiceDate())
                .dueDate(request.getDueDate())
                .subtotal(request.getSubtotal())
                .taxAmount(request.getTaxAmount())
                .discountAmount(request.getDiscountAmount())
                .totalAmount(request.getTotalAmount())
                .status(InvoiceStatus.PENDING)
                .description(request.getDescription())
                .notes(request.getNotes())
                .build();
        
        Invoice saved = invoiceRepository.save(invoice);
        
        // Auto-submit insurance claim
        autoClaimService.submitInsuranceClaim(saved);
        
        log.info("Invoice created: {}", saved.getId());
        return saved;
    }
    
    public Invoice getInvoiceById(String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
    
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    public List<Invoice> getInvoicesByPatient(String patientId) {
        return invoiceRepository.findByPatientId(patientId);
    }
    
    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }
    
    @Transactional
    public Invoice updateInvoiceStatus(String id, InvoiceStatus status) {
        Invoice invoice = getInvoiceById(id);
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public void deleteInvoice(String id) {
        invoiceRepository.deleteById(id);
    }
}
