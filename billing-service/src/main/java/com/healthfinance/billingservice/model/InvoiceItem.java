package com.healthfinance.billingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String invoiceId;
    
    @Column(nullable = false)
    private String serviceCode;
    
    @Column(nullable = false)
    private String serviceName;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
