package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.repository.filter.PaymentFilterFactory;
import com.radmiy.payment.service.app.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.radmiy.payment.service.app.model.PaymentStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private final Map<UUID, Optional<Payment>> map = new HashMap<>();

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Captor
    private ArgumentCaptor<UUID> paymentUuid;


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
        PaymentFilter paymentFilter = PaymentFilter.builder()
                .status(APPROVED)
                .build();
        Specification<Payment> spec =
                PaymentFilterFactory.fromFilter(paymentFilter);

        List<Payment> expected = map.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(payment -> payment.getStatus() == APPROVED)
                .toList();
        when(paymentRepository.findAll(isA(spec.getClass()))).thenReturn(expected);

        // when
        List<PaymentDto> payments = paymentService.search(paymentFilter);

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
                .status(APPROVED)
                .note("Test note")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        when(paymentRepository.save(any())).thenReturn(expectedPayment);

        PaymentDto newPayment = PaymentDto.builder()
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(APPROVED)
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

    @Test
    void deletePaymentTest() {
        // given
        UUID guid = map.keySet().iterator().next();

        // when
        paymentService.removePayment(guid);

        // then
        verify(paymentRepository).deleteById(paymentUuid.capture());
        UUID actualGuid = paymentUuid.getValue();
        assertEquals(guid, actualGuid);
    }

    private void initPayments() {
        UUID guid = UUID.randomUUID();
        map.put(guid, Optional.of(Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(APPROVED)
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
                .status(APPROVED)
                .note("Test note 3")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build()
        ));
    }
}
