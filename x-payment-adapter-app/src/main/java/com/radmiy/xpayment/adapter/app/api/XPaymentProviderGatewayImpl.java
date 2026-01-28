package com.radmiy.xpayment.adapter.app.api;

import com.radmiy.xpayment.adapter.app.api.client.DefaultApi;
import com.radmiy.xpayment.adapter.app.api.model.ChargeResponse;
import com.radmiy.xpayment.adapter.app.api.model.CreateChargeRequest;
import com.radmiy.xpayment.adapter.app.dto.ChargeResponseDto;
import com.radmiy.xpayment.adapter.app.dto.CreateChargeRequestDto;
import com.radmiy.xpayment.adapter.app.mapper.XPaymentApiMapper;
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
    private final XPaymentApiMapper apiMapper;

    @Override
    public ChargeResponseDto createCharge(CreateChargeRequestDto createChargeRequestDto) throws RestClientException {
        try {
            log.info("Send create charge request: {}", createChargeRequestDto);
            CreateChargeRequest createChargeRequest = apiMapper.requestDtoToRequest(createChargeRequestDto);
            ChargeResponse chargeResponse = defaultApi.createCharge(createChargeRequest);
            return apiMapper.responseToResponseDto(chargeResponse);
        } catch (Exception err) {
            log.error(err.getMessage());
            throw new RestClientException("Error in send create charge request", err);
        }
    }

    @Override
    public ChargeResponseDto getCharge(UUID id) throws RestClientException {
        try {
            log.info("Get charge request by id:{}", id);
            ChargeResponse chargeResponse = defaultApi.retrieveCharge(id);
            return apiMapper.responseToResponseDto(chargeResponse);
        } catch (Exception err) {
            log.error(err.getMessage());
            throw new RestClientException("Error in get charge request", err);
        }
    }
}
