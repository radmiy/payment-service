package com.radmiy.xpayment.adapter.app.checkstatus.mapper;

import com.radmiy.payment.service.api.dto.XPaymentAdapterResponseMessage;
import com.radmiy.xpayment.adapter.app.checkstatus.dto.PaymentStatusCheckMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentStatusCheckMessageMapper {

    @Mapping(target = "chargeGuid", source = "transactionRefId")
    PaymentStatusCheckMessage toMessage(XPaymentAdapterResponseMessage message);
}
