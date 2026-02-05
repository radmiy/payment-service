package com.radmiy.xpayment.adapter.app.checkstatus.handler;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.api.XPaymentProviderGateway;
import com.radmiy.xpayment.adapter.app.dto.ChargeResponseDto;
import com.radmiy.xpayment.adapter.app.mapper.XPaymentApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.radmiy.payment.service.api.XPaymentAdapterStatus.CANCELED;
import static com.radmiy.payment.service.api.XPaymentAdapterStatus.SUCCEEDED;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentStatusCheckerHandlerImpl implements PaymentStatusCheckerHandler {

    private final XPaymentProviderGateway providerGateway;
    private final AsyncSender<XPaymentAdapterResponseMessage> asyncSender;
    private final XPaymentApiMapper mapper;

    @Override
    public boolean handle(UUID chargeGuid) {
        ChargeResponseDto chargeResponseDto = providerGateway.getCharge(chargeGuid);

        if (chargeResponseDto != null &&
                (chargeResponseDto.getStatus() == SUCCEEDED || chargeResponseDto.getStatus() == CANCELED)) {
            asyncSender.send(mapper.responseToKafkaMessage(chargeResponseDto));
            return true;
        } else {
            return false;
        }
    }
}
