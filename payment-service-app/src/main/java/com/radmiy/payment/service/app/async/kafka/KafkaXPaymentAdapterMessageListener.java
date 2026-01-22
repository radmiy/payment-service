package com.radmiy.payment.service.app.async.kafka;

import com.radmiy.payment.service.api.AsyncListener;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.payment.service.app.async.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaXPaymentAdapterMessageListener implements AsyncListener<XPaymentAdapterResponseMessage> {

    private final MessageHandler<XPaymentAdapterResponseMessage> handler;

    @Override
    public void onMessage(XPaymentAdapterResponseMessage message) {
        handler.handle(message);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.xpayment-adapter.response-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(XPaymentAdapterResponseMessage message,
                        ConsumerRecord<String, XPaymentAdapterResponseMessage> record,
                        Acknowledgment acknowledgment) {
        try {
            log.info("Received XPayment Adapter response: {}", message);
            onMessage(message);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error handling XPayment Adapter response for paymentGuid = {}", message.getPaymentGuid(), e);
            throw e;
        }
    }
}
