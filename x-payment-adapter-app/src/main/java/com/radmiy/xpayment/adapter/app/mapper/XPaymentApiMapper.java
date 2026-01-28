package com.radmiy.xpayment.adapter.app.mapper;

import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.api.model.ChargeResponse;
import com.radmiy.xpayment.adapter.app.api.model.CreateChargeRequest;
import com.radmiy.xpayment.adapter.app.dto.ChargeResponseDto;
import com.radmiy.xpayment.adapter.app.dto.CreateChargeRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface XPaymentApiMapper {

    CreateChargeRequest requestDtoToRequest(CreateChargeRequestDto dto);

    ChargeResponseDto responseToResponseDto(ChargeResponse response);

    @Mapping(target = "paymentGuid", source = "order")
    @Mapping(target = "transactionRefId", source = "id")
    @Mapping(target = "occurredAt", expression = "java(java.time.OffsetDateTime.now())")
    XPaymentAdapterResponseMessage responseToKafkaMessage(ChargeResponseDto response);

    @Mapping(target = "order", source = "paymentGuid")
    CreateChargeRequestDto kafkaMessageToRequestDto(XPaymentAdapterRequestMessage request);
}
