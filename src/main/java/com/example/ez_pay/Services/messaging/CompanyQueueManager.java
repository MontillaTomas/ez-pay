package com.example.ez_pay.Services.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyQueueManager {

    private final AmqpAdmin amqpAdmin;

    public void registerCompanyQueue(Long companyId) {
        String queueName = "company." + companyId + ".queue";
        String routingKey = "company." + companyId + ".payment";

        Queue queue = new Queue(queueName, true);
        TopicExchange exchange = new TopicExchange("payment.notifications.exchange");

        Binding binding = BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);
    }
}
