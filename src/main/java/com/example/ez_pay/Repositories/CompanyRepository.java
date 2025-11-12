package com.example.ez_pay.Repositories;

import com.example.ez_pay.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
