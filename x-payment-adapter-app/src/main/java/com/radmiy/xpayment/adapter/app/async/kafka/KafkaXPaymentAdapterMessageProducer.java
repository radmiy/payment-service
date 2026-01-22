package com.radmiy.xpayment.adapter.app.async.kafka;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.config.kafka.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaXPaymentAdapterMessageProducer implements AsyncSender<XPaymentAdapterResponseMessage> {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, XPaymentAdapterResponseMessage> template;

    @Override
    public void send(XPaymentAdapterResponseMessage msg) {
        final String key = msg.getPaymentGuid().toString();
        log.info("Sending XPayment Adapter response: {} ", msg);

        template.send(kafkaProperties.getResponseTopic(), key, msg);
    }
}
