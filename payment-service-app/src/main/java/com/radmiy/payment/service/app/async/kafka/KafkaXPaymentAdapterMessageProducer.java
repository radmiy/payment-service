package com.radmiy.payment.service.app.async.kafka;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.app.config.kafka.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaXPaymentAdapterMessageProducer implements AsyncSender<XPaymentAdapterRequestMessage> {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, XPaymentAdapterRequestMessage> template;

    @Override
    public void send(XPaymentAdapterRequestMessage msg) {
        final String key = msg.getPaymentGuid().toString();
        log.info("Sending XPayment Adapter request: {} ", msg);

        log.info("Template: {}", template);
        log.info("\nKafka properties: {}", kafkaProperties);
        log.info("\nKey: {}", key);
        log.info("\nMsg: {}", msg);

        template.send(kafkaProperties.getRequestTopic(), key, msg);
    }
}
