package com.radmiy.xpayment.adapter.app.async.handler;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.api.XPaymentProviderGateway;
import com.radmiy.xpayment.adapter.app.dto.ChargeResponseDto;
import com.radmiy.xpayment.adapter.app.mapper.XPaymentApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandlerImpl implements MessageHandler<XPaymentAdapterRequestMessage> {

    private final AsyncSender<XPaymentAdapterResponseMessage> sender;
    private final XPaymentProviderGateway providerGateway;
    private final XPaymentApiMapper apiMapper;

    @Override
    public void handle(XPaymentAdapterRequestMessage requestMessage) {
        try {
            log.info("Send payment with id:{} to x-payment-api", requestMessage.getPaymentGuid());
            ChargeResponseDto chargeResponse =
                    providerGateway.createCharge(apiMapper.kafkaMessageToRequestDto(requestMessage));
            XPaymentAdapterResponseMessage adapterResponse = apiMapper.responseToKafkaMessage(chargeResponse);
            log.info("Sending XPayment Adapter message: {}", adapterResponse.getMessageId());
            sender.send(adapterResponse);
        } catch (Exception err) {
            log.error("Error send payment with id:{}", requestMessage.getPaymentGuid());
        }
    }
}
