package com.radmiy.payment.service.app.async;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryXPaymentAdapterMessageBroker implements AsyncSender<XPaymentAdapterRequestMessage> {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    private final AsyncListener<XPaymentAdapterResponseMessage>
            resultListener;

    @Override
    public void send(XPaymentAdapterRequestMessage request) {
        final UUID txId = UUID.randomUUID();
        final UUID messageId = UUID.randomUUID();
        log.info("Get request message for payment: {}", request);
        if (request.getAmount().remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0) {
            log.info("Payment amount is a multiple of two");
            scheduler.schedule(() -> emit(request, txId, messageId,
                    XPaymentAdapterStatus.PROCESSING), 0, TimeUnit.SECONDS);
            scheduler.schedule(() -> emit(request, txId, messageId,
                    XPaymentAdapterStatus.PROCESSING), 10, TimeUnit.SECONDS);

            scheduler.schedule(() -> emit(request, txId, messageId,
                    XPaymentAdapterStatus.SUCCEEDED), 20, TimeUnit.SECONDS);
        } else {
            log.info("Payment amount is not a multiple of two");
            scheduler.schedule(() -> emit(request, txId, messageId,
                    XPaymentAdapterStatus.CANCELED), 30, TimeUnit.SECONDS);
        }
    }

    private void emit(XPaymentAdapterRequestMessage request, UUID txId, UUID messageId,
                      XPaymentAdapterStatus status) {
        log.info("Convert request message to response.");
        final XPaymentAdapterResponseMessage result = new XPaymentAdapterResponseMessage();
        result.setMessageGuid(messageId);
        result.setPaymentGuid(request.getPaymentGuid());
        result.setAmount(request.getAmount());
        result.setCurrency(request.getCurrency());
        result.setTransactionRefId(txId);
        result.setStatus(status);
        result.setOccurredAt(OffsetDateTime.now());
        resultListener.onMessage(result);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
