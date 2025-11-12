package com.example.ez_pay.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ez_pay.Models.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findByCompanyCompanyId(Long companyId);
}
