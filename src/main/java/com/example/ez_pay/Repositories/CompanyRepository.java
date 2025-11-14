package com.example.ez_pay.Repositories;

import com.example.ez_pay.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByUserId(Long userId);
    Optional<Company> findByCuit(String cuit);
}
