package com.radmiy.xpayment.adapter.app.checkstatus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class RabbitMqPaymentRetryConfig {

    private final RabbitMqProperties properties;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue xPaymentQueue() {
        return QueueBuilder.durable(properties.getQueueName())
                .withArgument("x-dead-letter-exchange", "payments.dlx")
                .withArgument("x-dead-letter-routing-key", "payments.dead")
                .build();
    }

    @Bean
    public CustomExchange delayExchange() {
        return new CustomExchange(
                properties.getExchangeName(),
                "x-delayed-message",
                true,
                false,
                Map.of("x-delayed-type", "direct"));
    }

    @Bean
    public Binding queueBinding(Queue xPaymentQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(xPaymentQueue)
                .to(delayedExchange)
                .with(properties.getQueueName())
                .noargs();
    }
}
