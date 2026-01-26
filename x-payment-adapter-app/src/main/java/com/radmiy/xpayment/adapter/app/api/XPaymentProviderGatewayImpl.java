package com.radmiy.xpayment.adapter.app.api;

import com.radmiy.xpayment.adapter.app.api.client.DefaultApi;
import com.radmiy.xpayment.adapter.app.api.model.ChargeResponse;
import com.radmiy.xpayment.adapter.app.api.model.CreateChargeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class XPaymentProviderGatewayImpl implements XPaymentProviderGateway {

    private final DefaultApi defaultApi;


    @Override
    public ChargeResponse createCharge(CreateChargeRequest createChargeRequest) throws RestClientException {
        try {
            log.info("Send create charge request: {}", createChargeRequest);
            return defaultApi.createCharge(createChargeRequest);
        } catch (Exception err) {
            throw new RestClientException("Error in send create charge request", err);
        }
    }

    @Override
    public ChargeResponse getCharge(UUID id) throws RestClientException {
        try {
            log.info("Get charge request by id:{}", id);
            return defaultApi.retrieveCharge(id);
        } catch (Exception err) {
            throw new RestClientException("Error in get charge request", err);
        }
    }
}
