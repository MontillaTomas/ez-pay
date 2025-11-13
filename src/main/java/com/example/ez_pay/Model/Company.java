package com.example.ez_pay.Model;

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

    public Company(Category category, String address, String province, String city, int monthlyInvoices, String cuit, String legalName, int numberOfPayments, BigDecimal averageInvoice, UserEntity user) {
        this.category = category;
        this.address = address;
        this.province = province;
        this.city = city;
        this.monthlyInvoices = monthlyInvoices;
        this.cuit = cuit;
        this.legalName = legalName;
        this.numberOfPayments = numberOfPayments;
        this.averageInvoice = averageInvoice;
        this.user = user;
    }
}
