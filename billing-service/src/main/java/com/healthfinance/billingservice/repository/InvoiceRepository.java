package com.healthfinance.billingservice.repository;

import com.healthfinance.billingservice.model.Invoice;
import com.healthfinance.billingservice.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    
    List<Invoice> findByPatientId(String patientId);
    
    List<Invoice> findByPatientIdAndStatus(String patientId, InvoiceStatus status);
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByStatus(InvoiceStatus status);
    
    List<Invoice> findByDueDateBefore(LocalDate date);
    
    @Query("SELECT i FROM Invoice i WHERE i.patientId = :patientId AND i.status = 'OVERDUE'")
    List<Invoice> findOverdueInvoicesByPatient(@Param("patientId") String patientId);
    
    List<Invoice> findByPatientIdOrderByInvoiceDateDesc(String patientId);
}
