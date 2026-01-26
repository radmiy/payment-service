package com.radmiy.xpayment.adapter.app.async.kafka;

import com.radmiy.payment.service.api.AsyncListener;
import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.xpayment.adapter.app.async.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaXPaymentAdapterMessageListener implements AsyncListener<XPaymentAdapterRequestMessage> {

    private final MessageHandler<XPaymentAdapterRequestMessage> handler;

    @Override
    public void onMessage(XPaymentAdapterRequestMessage message) {
        handler.handle(message);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.xpayment-adapter.request-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(XPaymentAdapterRequestMessage message,
                        ConsumerRecord<String, XPaymentAdapterRequestMessage> record,
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
