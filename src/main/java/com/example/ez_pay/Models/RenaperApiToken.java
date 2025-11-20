package com.example.ez_pay.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class RenaperApiToken {
    @Id
    private Long id;
    private String token;
    private Integer remainingUses;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
