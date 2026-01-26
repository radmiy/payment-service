package com.radmiy.xpayment.adapter.app.api;

import com.radmiy.xpayment.adapter.app.api.model.ChargeResponse;
import com.radmiy.xpayment.adapter.app.api.model.CreateChargeRequest;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

public interface XPaymentProviderGateway {

    ChargeResponse createCharge(CreateChargeRequest createChargeRequest) throws RestClientException;

    ChargeResponse getCharge(UUID id) throws RestClientException;
}
