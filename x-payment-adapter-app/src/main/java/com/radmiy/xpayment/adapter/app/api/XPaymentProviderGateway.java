package com.radmiy.xpayment.adapter.app.api;

import com.radmiy.xpayment.adapter.app.dto.ChargeResponseDto;
import com.radmiy.xpayment.adapter.app.dto.CreateChargeRequestDto;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

public interface XPaymentProviderGateway {

    ChargeResponseDto createCharge(CreateChargeRequestDto createChargeRequest) throws RestClientException;

    ChargeResponseDto getCharge(UUID id) throws RestClientException;
}
