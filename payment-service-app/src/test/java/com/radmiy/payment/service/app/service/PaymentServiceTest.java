package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private final Map<UUID, Optional<Payment>> map = new HashMap<>();

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;


    @BeforeEach
    void setUp() {
        initPayments();
    }

    @Test
    void getPaymentTest() {
        // given
        UUID guid = map.keySet().iterator().next();
        Payment expected = map.get(guid).get();
        when(paymentRepository.findById(any())).thenReturn(Optional.of(expected));

        // when
        PaymentDto payment = paymentService.getPayment(guid);

        // then
        assertNotNull(payment);
        assertEquals(expected.getGuid(), payment.getGuid());
    }

    @Test
    void getAllPaymentsTest() {
        // given
        List<Payment> expected = map.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        when(paymentRepository.findAll()).thenReturn(expected);

        // when
        List<PaymentDto> payments = paymentService.getPayments();

        // then
        assertNotNull(payments);
        assertEquals(3, payments.size());

    }

    @Test
    void getPaymentsByStatusTest() {
        // given
        List<Payment> expected = map.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(payment -> payment.getStatus() ==  PaymentStatus.APPROVED)
                .toList();
        when(paymentRepository.findAll()).thenReturn(expected);

        // when
        List<PaymentDto> payments = paymentService.getPayments();

        // then
        assertNotNull(payments);
        assertEquals(2, payments.size());

    }

    @Test
    void createPaymentTest() {
        // given
        UUID guid = UUID.randomUUID();
        Payment expectedPayment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(PaymentStatus.APPROVED)
                .note("Test note")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        when(paymentRepository.save(any())).thenReturn(expectedPayment);

        PaymentDto newPayment = PaymentDto.builder()
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(PaymentStatus.APPROVED)
                .note("Test note")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        // when
        PaymentDto result = paymentService.addPayment(newPayment);

        // then
        assertNotNull(result);
        assertEquals(expectedPayment.getGuid(), result.getGuid());
    }

    private void initPayments() {
        UUID guid = UUID.randomUUID();
        map.put(guid, Optional.of(Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(PaymentStatus.APPROVED)
                .note("Test note 1")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build()));
        guid = UUID.randomUUID();
        map.put(guid, Optional.of(Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(2000))
                .currency("USD")
                .status(PaymentStatus.DECLINED)
                .note("Test note 2")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build()
        ));
        guid = UUID.randomUUID();
        map.put(guid, Optional.of(Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(3000))
                .currency("EUR")
                .status(PaymentStatus.APPROVED)
                .note("Test note 3")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build()
        ));
    }
}
