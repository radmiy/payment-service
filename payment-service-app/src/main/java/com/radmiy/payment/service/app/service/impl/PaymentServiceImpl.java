package com.radmiy.payment.service.app.service.impl;

import com.radmiy.payment.service.app.async.AsyncSender;
import com.radmiy.payment.service.app.async.XPaymentAdapterMapper;
import com.radmiy.payment.service.app.async.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.app.exception.ServiceException;
import com.radmiy.payment.service.app.mapper.PaymentMapper;
import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.repository.filter.PaymentFilterFactory;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.radmiy.payment.service.app.exception.Error.NULL_ID;
import static com.radmiy.payment.service.app.exception.Error.PAYMENT_IS_NUL;
import static com.radmiy.payment.service.app.exception.Error.PAYMENT_NOT_EXIST;
import static com.radmiy.payment.service.app.model.PaymentStatus.NOT_SENT;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final XPaymentAdapterMapper xPaymentAdapterMapper;
    private final AsyncSender<XPaymentAdapterRequestMessage> sender;

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        checkDto(dto);

        final Payment payment = paymentMapper.toEntity(dto);
        payment.setGuid(UUID.randomUUID());
        payment.setInquiryRefId(UUID.randomUUID());
        payment.setTransactionRefId(UUID.randomUUID());
        payment.setCreatedAt(OffsetDateTime.now());
        payment.setUpdatedAt(OffsetDateTime.now());
        if (payment.getAmount() == null) {
            payment.setAmount(BigDecimal.ZERO);
        }
        if (payment.getCurrency() == null || payment.getCurrency().isBlank()) {
            payment.setCurrency("USD");
        }
        if (payment.getStatus() == null) {
            payment.setStatus(NOT_SENT);
        }

        final PaymentDto paymentDto = paymentMapper.toDto(paymentRepository.save(payment));
        final XPaymentAdapterRequestMessage requestMessage =
                xPaymentAdapterMapper.toXPaymentAdapterRequestMessage(payment);
        log.info("Send request message for payment: {}", requestMessage);
        sender.send(requestMessage);

        return paymentDto;
    }

    @Override
    public PaymentDto getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDto)
                .orElseThrow(() -> new ServiceException(PAYMENT_NOT_EXIST, id));
    }

    @Override
    public PaymentDto updatePayment(UUID id, PaymentDto dto) {
        checkId(id);
        checkDto(dto);

        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ServiceException(PAYMENT_NOT_EXIST, id));
        payment.setUpdatedAt(OffsetDateTime.now());
        payment.setStatus(dto.getStatus());
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setNote(dto.getNote());

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    private void checkDto(PaymentDto dto) {
        if (dto == null) {
            throw new ServiceException(PAYMENT_IS_NUL);
        }
    }

    private void checkId(UUID id) {
        if (id == null) {
            throw new ServiceException(NULL_ID);
        } else if (!paymentRepository.existsById(id)) {
            throw new ServiceException(PAYMENT_NOT_EXIST, id);
        }
    }

    @Override
    public void deletePayment(UUID id) {
        checkId(id);

        paymentRepository.deleteById(id);
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
    public PaymentDto updateStatus(UUID id, PaymentStatus status) {
        checkId(id);
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ServiceException(PAYMENT_NOT_EXIST, id));
        payment.setStatus(status);
        payment.setUpdatedAt(OffsetDateTime.now());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto updateNote(UUID id, String note) {
        checkId(id);
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ServiceException(PAYMENT_NOT_EXIST, id));
        payment.setNote(note);
        payment.setUpdatedAt(OffsetDateTime.now());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }
}
