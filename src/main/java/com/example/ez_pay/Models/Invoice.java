package com.example.ez_pay.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // datos del emisor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    //datos del destinatario --  consumir renaper para validar datos
    @Column(nullable = false)
    private String receiverName;
    @Column(nullable = false)
    private String receiverCUIL;

    @Column(nullable = false)
    private BigDecimal amount;
    private BigDecimal secondAmount;

    @Column(nullable = false, updatable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;
    private LocalDate secondDueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "payment_stub_id", unique = true)
    private PaymentStub paymentStub;
}
