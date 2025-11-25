package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.exception.PaymentNotFoundException;
import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto getPayment(long id) {
        Optional<PaymentModel> payment = Optional.ofNullable(paymentRepository.getPayment(id));
        if (payment.isEmpty()) {
            throw new PaymentNotFoundException("Payment not found");
        }
        return convertToDto(payment);
    }

    @Override
    public List<PaymentDto> getPayments() {
        List<PaymentModel> models = paymentRepository.getPayments();

        return models.stream()
                .filter(Objects::nonNull)
                .map(Optional::of)
                .map(PaymentServiceImpl::convertToDto)
                .toList();
    }

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        if (paymentDto == null) {
            throw new IllegalArgumentException("Payment is not valid");
        }

        Optional<PaymentModel> paymentModel =
                Optional.ofNullable(
                        paymentRepository.addPayment(convertToModel(Optional.of(paymentDto)))
                );
        return convertToDto(paymentModel);
    }

    @Override
    public boolean removePayment(long id) {
        boolean removePayment = paymentRepository.removePayment(id);
        if (!removePayment) {
            throw new PaymentNotFoundException("Payment not found");
        }

        return removePayment;
    }

    private static PaymentDto convertToDto(Optional<PaymentModel> payment) {
        if (payment.isEmpty()) {
            throw new IllegalArgumentException();
        }

        PaymentModel paymentModel = payment.get();

        PaymentDto dto = PaymentDto.builder()
                .value(paymentModel.getValue())
                .name(paymentModel.getName())
                .build();
        if (paymentModel.getPaymentId() != null) {
            dto.setPaymentId(paymentModel.getPaymentId());
        }
        return dto;
    }

    private static PaymentModel convertToModel(Optional<PaymentDto> dto) {
        if (dto.isEmpty()) {
            throw new IllegalArgumentException();
        }

        PaymentDto paymentDto = dto.get();

        return PaymentModel.builder()
                .value(paymentDto.getValue())
                .name(paymentDto.getName())
                .build();
    }
}
