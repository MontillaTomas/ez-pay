package com.example.ez_pay.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStub {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 42, nullable = false)
    private String electronicPaymentCode;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BarcodeType barcodeType;
    @Column(nullable = false)
    private LocalDate createdAt;
}
