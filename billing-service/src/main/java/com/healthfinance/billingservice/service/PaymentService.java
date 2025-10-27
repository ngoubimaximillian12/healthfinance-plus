package com.healthfinance.billingservice.service;

import com.healthfinance.billingservice.dto.PaymentRequest;
import com.healthfinance.billingservice.dto.PaymentResponse;
import com.healthfinance.billingservice.model.Invoice;
import com.healthfinance.billingservice.model.InvoiceStatus;
import com.healthfinance.billingservice.model.Payment;
import com.healthfinance.billingservice.model.PaymentStatus;
import com.healthfinance.billingservice.repository.InvoiceRepository;
import com.healthfinance.billingservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating payment for invoice: {}", request.getInvoiceId());
        
        Payment payment = Payment.builder()
                .transactionId(request.getTransactionId())
                .invoiceId(request.getInvoiceId())
                .patientId(request.getPatientId())
                .paymentDate(request.getPaymentDate())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.COMPLETED)
                .cardLast4Digits(request.getCardLast4Digits())
                .cardType(request.getCardType())
                .checkNumber(request.getCheckNumber())
                .referenceNumber(request.getReferenceNumber())
                .notes(request.getNotes())
                .processedDate(LocalDate.now())
                .build();
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Update invoice
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        BigDecimal newAmountPaid = invoice.getAmountPaid().add(request.getAmount());
        BigDecimal newAmountDue = invoice.getAmountDue().subtract(request.getAmount());
        
        invoice.setAmountPaid(newAmountPaid);
        invoice.setAmountDue(newAmountDue);
        
        if (newAmountDue.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaidDate(LocalDate.now());
        } else if (newAmountPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        
        invoiceRepository.save(invoice);
        
        log.info("Payment created with ID: {}", savedPayment.getId());
        return PaymentResponse.fromPayment(savedPayment);
    }
    
    public PaymentResponse getPaymentById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return PaymentResponse.fromPayment(payment);
    }
    
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
    }
    
    public List<PaymentResponse> getPaymentsByPatient(String patientId) {
        return paymentRepository.findByPatientIdOrderByPaymentDateDesc(patientId).stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
    }
    
    public List<PaymentResponse> getPaymentsByInvoice(String invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deletePayment(String id) {
        paymentRepository.deleteById(id);
    }
}
