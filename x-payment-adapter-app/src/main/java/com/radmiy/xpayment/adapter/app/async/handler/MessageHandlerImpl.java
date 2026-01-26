package com.radmiy.xpayment.adapter.app.async.handler;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.XPaymentAdapterStatus;
import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.api.XPaymentProviderGateway;
import com.radmiy.xpayment.adapter.app.api.model.ChargeResponse;
import com.radmiy.xpayment.adapter.app.api.model.CreateChargeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandlerImpl implements MessageHandler<XPaymentAdapterRequestMessage> {

    private final AsyncSender<XPaymentAdapterResponseMessage> sender;
    private final XPaymentProviderGateway providerGateway;

    @Override
    public void handle(XPaymentAdapterRequestMessage requestMessage) {
        CreateChargeRequest chargeRequest = getCreateChargeRequest(requestMessage);
        try {
            log.info("Send payment with id:{} to x-payment-api", requestMessage.getPaymentGuid());
            ChargeResponse chargeResponse = providerGateway.createCharge(chargeRequest);
            XPaymentAdapterResponseMessage adapterResponse = getXPaymentAdapterResponse(chargeResponse);
            log.info("Sending XPayment Adapter message: {}", adapterResponse.getMessageId());
            sender.send(adapterResponse);
        } catch (Exception err) {
            log.error("Error send payment with id:{}", chargeRequest.getOrder());
        }
    }

    private static CreateChargeRequest getCreateChargeRequest(XPaymentAdapterRequestMessage requestMessage) {
        CreateChargeRequest chargeRequest = new CreateChargeRequest();
        chargeRequest.setAmount(requestMessage.getAmount());
        chargeRequest.setCurrency(requestMessage.getCurrency());
        chargeRequest.setOrder(requestMessage.getPaymentGuid());
        return chargeRequest;
    }

    private XPaymentAdapterResponseMessage getXPaymentAdapterResponse(ChargeResponse chargeResponse) {
        XPaymentAdapterResponseMessage responseMessage = new XPaymentAdapterResponseMessage();
        responseMessage.setMessageGuid(UUID.randomUUID());
        responseMessage.setPaymentGuid(chargeResponse.getOrder());
        responseMessage.setTransactionRefId(chargeResponse.getId());
        responseMessage.setAmount(chargeResponse.getAmount());
        responseMessage.setCurrency(chargeResponse.getCurrency());
        responseMessage.setStatus(XPaymentAdapterStatus.valueOf(chargeResponse.getStatus()));
        responseMessage.setOccurredAt(OffsetDateTime.now());
        return responseMessage;
    }
}
