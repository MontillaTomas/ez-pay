package com.example.ez_pay.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private int monthlyInvoices;
    @Column(nullable = false, unique = true)
    private String cuit;
    @Column(nullable = false)
    private String legalName;
    @Column(nullable = false)
    private int numberOfPayments;
    @Column(nullable = false)
    private BigDecimal averageInvoice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Invoice> invoices;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}