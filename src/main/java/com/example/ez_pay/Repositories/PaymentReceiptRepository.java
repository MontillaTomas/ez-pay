package com.example.ez_pay.Repositories;

import com.example.ez_pay.Models.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, UUID> {
}
