package com.radmiy.payment.service.app.mapper;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public abstract class PaymentMapperDecorator implements PaymentMapper {

    @Autowired
    private PaymentMapper delegate;

    @Override
    public PaymentDto toDto(Payment payment) {
        final PaymentDto dto = delegate.toDto(payment);
        if (!"RUB".equalsIgnoreCase(payment.getCurrency())) {
            dto.setConverted(dto.getAmount().multiply(new BigDecimal(3)));
        }
        return dto;
    }

    @Override
    public Payment toEntity(PaymentDto dto) {
        final Payment payment = delegate.toEntity(dto);

        return payment;
    }

}
