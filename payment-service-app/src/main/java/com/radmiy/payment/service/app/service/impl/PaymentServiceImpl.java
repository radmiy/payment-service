package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.exception.PaymentNotFoundException;
import com.radmiy.payment.service.app.mapper.PaymentMapper;
import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.repository.filter.PaymentFilterFactory;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDto)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    @Override
    public List<PaymentDto> getPayments() {
        return paymentRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentDto> search(PaymentFilter filter) {
        final Specification<Payment> spec =
                PaymentFilterFactory.fromFilter(filter);
        return paymentRepository.findAll(spec).stream()
                .filter(Objects::nonNull)
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public Page<PaymentDto> searchPaged(PaymentFilter filter, Pageable pageable) {
        final Specification<Payment> spec = PaymentFilterFactory.fromFilter(filter);
        return paymentRepository.findAll(spec, pageable)
                .map(paymentMapper::toDto);
    }

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        if (paymentDto == null) {
            throw new IllegalArgumentException("Payment is not valid");
        }

        final Payment paymentModel = paymentRepository.save(paymentMapper.toEntity(paymentDto));
        return paymentMapper.toDto(paymentModel);
    }

    @Override
    public void removePayment(UUID id) {
        paymentRepository.deleteById(id);
    }
}
