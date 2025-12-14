package com.radmiy.payment.service.app.mapper;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(PaymentMapperDecorator.class)
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto dto);
}
