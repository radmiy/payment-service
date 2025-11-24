package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto getPayment(long id) {
        PaymentModel model = paymentRepository.getPayment(id);
        if (model == null) {
            return null;
        }
        return convertToDto(model);
    }

    @Override
    public List<PaymentDto> getPayments() {
        List<PaymentModel> models = paymentRepository.getPayments();

        return models.stream()
                .filter(Objects::nonNull)
                .map(PaymentServiceImpl::convertToDto)
                .toList();
    }

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        PaymentModel payment = convertToModel(paymentDto);
        return convertToDto(paymentRepository.addPayment(payment));
    }

    @Override
    public boolean removePayment(long id) {
        return paymentRepository.removePayment(id);
    }

    private static PaymentDto convertToDto(PaymentModel payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto dto = PaymentDto.builder()
                .value(payment.getValue())
                .name(payment.getName())
                .build();
        if (payment.getPaymentId() != null) {
            dto.setPaymentId(payment.getPaymentId());
        }
        return dto;
    }

    private static PaymentModel convertToModel(PaymentDto dto) {
        return PaymentModel.builder()
                .value(dto.getValue())
                .name(dto.getName())
                .build();
    }
}
