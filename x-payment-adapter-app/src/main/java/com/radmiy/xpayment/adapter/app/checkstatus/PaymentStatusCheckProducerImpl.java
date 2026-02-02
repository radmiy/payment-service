package com.radmiy.xpayment.adapter.app.checkstatus;

import com.radmiy.xpayment.adapter.app.checkstatus.config.RabbitMqProperties;
import com.radmiy.xpayment.adapter.app.checkstatus.dto.PaymentStatusCheckMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentStatusCheckProducerImpl implements PaymentStatusCheckProducer {

    private final RabbitMqProperties properties;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentStatusCheckProducerImpl(
            RabbitTemplate rabbitTemplate,
            RabbitMqProperties properties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }


    @Override
    public void send(PaymentStatusCheckMessage statusCheckMessage) {
        rabbitTemplate.convertAndSend(
                properties.getExchangeName(),
                properties.getQueueName(),
                statusCheckMessage,
                message -> {
                    message.getMessageProperties().setHeader("x-delay", properties.getIntervalMs());
                    message.getMessageProperties().setHeader("x-retry-count", 1);
                    return message;
                }
        );
    }
}
