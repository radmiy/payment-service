package com.radmiy.payment.service.app.mapper;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public abstract class PaymentMapperDecorator implements PaymentMapper {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentDto toDto(Payment payment) {
        final PaymentDto dto = paymentMapper.toDto(payment);
        if (!"RUB".equalsIgnoreCase(payment.getCurrency())) {
            dto.setConverted(dto.getAmount().multiply(new BigDecimal(3)));
        }
        return dto;
    }

    @Override
    public Payment toEntity(PaymentDto dto) {
        final Payment payment = paymentMapper.toEntity(dto);

        return payment;
    }

}
