package com.radmiy.xpayment.adapter.app.checkstatus;

import com.radmiy.xpayment.adapter.app.checkstatus.config.RabbitMqProperties;
import com.radmiy.xpayment.adapter.app.checkstatus.dto.PaymentStatusCheckMessage;
import com.radmiy.xpayment.adapter.app.checkstatus.handler.PaymentStatusCheckerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStatusCheckListener {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties properties;
    private final PaymentStatusCheckerHandler checkerHandler;

    @RabbitListener(queues = "${app.rabbitmq.queue-name}")
    public void handle(PaymentStatusCheckMessage statusCheckMessage, Message raw) {
        MessageProperties props = raw.getMessageProperties();

        int retryCount = (int) props.getHeaders().getOrDefault("x-retry-count", 0);
        boolean paid = checkerHandler.handle(statusCheckMessage.getChargeGuid());
        if (paid) {
            return;
        }

        if ((retryCount < properties.getMaxRetries())) {
            rabbitTemplate.convertAndSend(
                    properties.getExchangeName(),
                    properties.getQueueName(),
                    statusCheckMessage.clone(),
                    message -> {
                        message.getMessageProperties().setHeader("x-delay", properties.getIntervalMs());
                        message.getMessageProperties().setHeader("x-retry-count", retryCount + 1);
                        return message;
                    }
            );
        } else {
            rabbitTemplate.convertAndSend(
                    properties.getExchangeName(),
                    properties.getQueueName(),
                    statusCheckMessage,
                    message -> {

                        message.getMessageProperties().setHeader("x-retry-count", retryCount);
                        message.getMessageProperties().setHeader("x-final-status", "TIMEOUT");
                        message.getMessageProperties().setHeader("x-original-queue", props.getConsumerQueue());
                        return message;
                    }
            );
        }
    }
}
