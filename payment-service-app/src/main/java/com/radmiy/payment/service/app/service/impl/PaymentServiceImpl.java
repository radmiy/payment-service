package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.exception.PaymentNotFoundException;
import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(PaymentServiceImpl::convertToDto)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    @Override
    public List<PaymentDto> getPayments() {
        return paymentRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(PaymentServiceImpl::convertToDto)
                .toList();
    }

    @Override
    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .filter(Objects::nonNull)
                .map(PaymentServiceImpl::convertToDto)
                .toList();
    }

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        if (paymentDto == null) {
            throw new IllegalArgumentException("Payment is not valid");
        }

        final Payment paymentModel = paymentRepository.save(convertToModel(paymentDto));
        return convertToDto(paymentModel);
    }

    @Override
    public void removePayment(UUID id) {
        paymentRepository.deleteById(id);
    }

    private static PaymentDto convertToDto(Payment payment) {
        final PaymentDto dto = PaymentDto.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .note(payment.getNote())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
        if (payment.getGuid() != null) {
            dto.setGuid(payment.getGuid());
        }
        return dto;
    }

    private static Payment convertToModel(PaymentDto payment) {
        return Payment.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .note(payment.getNote())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
