package com.healthfinance.billingservice.repository;

import com.healthfinance.billingservice.model.Payment;
import com.healthfinance.billingservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    List<Payment> findByPatientId(String patientId);
    
    List<Payment> findByInvoiceId(String invoiceId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Payment> findByPatientIdOrderByPaymentDateDesc(String patientId);
}
