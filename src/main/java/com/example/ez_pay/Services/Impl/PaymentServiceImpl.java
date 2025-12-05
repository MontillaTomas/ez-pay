package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.Request.InvoicePaymentRequest;
import com.example.ez_pay.DTOs.Response.InvoicePaymentResponse;
import com.example.ez_pay.Exceptions.InvalidPaymentAmountException;
import com.example.ez_pay.Exceptions.NotAuthenticatedException;
import com.example.ez_pay.Exceptions.InvoiceCannotBePaidException;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.PaymentReceiptMapper;
import com.example.ez_pay.Models.*;
import com.example.ez_pay.Notifications.PaymentNotification;
import com.example.ez_pay.Repositories.EmployeeRepository;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Repositories.PaymentReceiptRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.messaging.PaymentNotificationProducer;
import com.example.ez_pay.Services.PaymentService;
import com.example.ez_pay.Services.PaymentStubService;
import com.example.ez_pay.ValueObject.PaymentCalculationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;
    private final PaymentStubService paymentStubService;
    private final PaymentReceiptMapper paymentReceiptMapper;
    private final PaymentNotificationProducer paymentNotificationProducer;

    @Override
    @Transactional
    public InvoicePaymentResponse processInvoicePayment(InvoicePaymentRequest invoicePaymentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("Usuario no autenticado.");
        }
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nombre: " + username));

        Employee emp = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado para el usuario con ID: " + user.getId()));

        Invoice invoice = invoiceRepository.findByPaymentStubElectronicPaymentCode(invoicePaymentRequest.getEpc())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada para el CEP: " + invoicePaymentRequest.getEpc()));

        PaymentCalculationResult paymentData = paymentStubService.calculatePaymentStubData(invoice);

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new InvoiceCannotBePaidException("La factura ya ha sido pagada.");
        }

        if (!paymentData.isCanBePaid() || invoice.getStatus() == InvoiceStatus.OVERDUE) {
            throw new InvoiceCannotBePaidException("La factura no puede ser pagada porque ha vencido su plazo de pago.");
        }

        if (invoicePaymentRequest.getAmountReceived() != null &&
                !invoicePaymentRequest.getAmountReceived().equals(paymentData.getAmountToApply())) {
            throw new InvalidPaymentAmountException("El monto recibido no coincide con el monto a pagar de la factura.");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setInvoice(invoice);
        receipt.setPaymentDateTime(LocalDateTime.now());
        receipt.setEmployee(emp);
        receipt.setPaymentPoint(emp.getPaymentPoint());
        receipt.setAmountPaid(paymentData.getAmountToApply());

        PaymentReceipt receiptSaved = paymentReceiptRepository.save(receipt);

        Long companyId = receiptSaved.getInvoice().getCompany().getCompanyId();
        PaymentNotification notification = new PaymentNotification(
                receiptSaved.getId(),
                invoicePaymentRequest.getEpc(),
                companyId,
                receiptSaved.getPaymentDateTime(),
                "PAID",
                receiptSaved.getInvoice().getReceiverName(),
                receiptSaved.getInvoice().getReceiverCUIL(),
                emp.getId(),
                emp.getPaymentPoint().getId());
        paymentNotificationProducer.sendPaymentNotification(companyId, notification);

        return paymentReceiptMapper.toResponse(receiptSaved);
    }
}
