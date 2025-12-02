package com.example.ez_pay.Services.messaging;

import com.example.ez_pay.Config.RabbitMQConfig;
import com.example.ez_pay.Notifications.PaymentNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentNotification(Long companyId, PaymentNotification payload) {

        String routingKey = "company." + companyId + ".payment";

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                payload
        );
    }
}

