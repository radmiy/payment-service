package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.Repository;
import com.radmiy.payment.service.app.service.Service;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class PaymentService implements Service {

    private final Repository paymentRepository;

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
                .map(PaymentService::convertToDto)
                .toList();
    }

    @Override
    public void addPayment(PaymentDto paymentDto) {
        paymentRepository.addPayment(convertToModel(paymentDto));
    }

    @Override
    public void removePayment(long id) {
        paymentRepository.removePayment(id);
    }

    private static PaymentDto convertToDto(PaymentModel model) {
        return PaymentDto.builder()
                .paymentId(model.getPaymentId())
                .value(model.getValue())
                .name(model.getName())
                .build();
    }

    private static PaymentModel convertToModel(PaymentDto dto) {
        return PaymentModel.builder()
                .value(dto.getValue())
                .name(dto.getName())
                .build();
    }
}
