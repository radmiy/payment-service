package com.radmiy.payment.service.app.async.handler;

import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandlerImpl implements MessageHandler<XPaymentAdapterResponseMessage> {

    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void handle(XPaymentAdapterResponseMessage message) {

        final PaymentStatus status = switch (message.getStatus()) {
            case PROCESSING -> PaymentStatus.PENDING;
            case CANCELED -> PaymentStatus.DECLINED;
            case SUCCEEDED -> PaymentStatus.APPROVED;
        };

        log.info("Update payment status to: {}", status);
        paymentService.updateStatus(message.getPaymentGuid(), status);
    }
}
