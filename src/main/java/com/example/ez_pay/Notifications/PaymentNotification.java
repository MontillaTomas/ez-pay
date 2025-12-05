package com.example.ez_pay.Notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentNotification {
    private UUID paymentReceiptId;
    private String epc;
    private Long companyId;
    private LocalDateTime paymentDate;
    private String status;
    private String receiverName;
    private String receiverCUIL;
    private Long employeeId;
    private Long paymentPointId;
}
