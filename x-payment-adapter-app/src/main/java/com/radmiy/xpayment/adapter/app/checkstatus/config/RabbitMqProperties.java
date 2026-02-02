package com.radmiy.xpayment.adapter.app.checkstatus.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitMqProperties {

    private String queueName;
    private String exchangeName;
    private Integer maxRetries;
    private Long intervalMs;
}
