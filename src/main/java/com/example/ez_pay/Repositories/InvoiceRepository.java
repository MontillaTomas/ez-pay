package com.example.ez_pay.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ez_pay.Models.Invoice;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findByCompanyCompanyId(Long companyId, Pageable pageable);
    Optional<Invoice> findByPaymentStubElectronicPaymentCode(String electronicPaymentCode);

    Optional<Invoice> findByClientIdentifierAndCompanyCompanyId(String clientIdentifier, Long companyId);

}
