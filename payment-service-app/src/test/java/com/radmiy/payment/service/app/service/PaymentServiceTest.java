package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private final Map<Long, PaymentModel> map = new HashMap<>();

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;


    @BeforeEach
    void setUp() {
        initPayments();
    }

    @Test
    void getPayment() {
        // given
        Long paymentId = 1L;

        // when
        when(paymentRepository.getPayment(anyLong())).thenReturn(map.get(paymentId));
        PaymentDto payment = paymentService.getPayment(paymentId);

        // then
        assertEquals("First record", payment.getName());
        assertEquals(paymentId, payment.getPaymentId());
        assertEquals(1_000, payment.getValue());
    }

    @Test
    void getAllPayments() {
        // given
        when(paymentRepository.getPayments()).thenReturn(new ArrayList<>(map.values()));

        // when
        List<PaymentDto> payments = paymentService.getPayments();

        // then
        assertNotNull(payments);
        assertEquals(3, payments.size());

    }

    @Test
    void createPayment() {
        // given
        PaymentDto newPayment = PaymentDto.builder()
                .value(1_000.0)
                .name("Other record")
                .build();
        PaymentModel expectedPayment = PaymentModel.builder()
                .paymentId(1_000L)
                .value(1_000.0)
                .name("Other record")
                .build();

        // when
        when(paymentRepository.addPayment(any())).thenReturn(expectedPayment);
        PaymentDto result = paymentService.addPayment(newPayment);

        // then
        assertNotNull(result);
        assertEquals(expectedPayment.getPaymentId(), result.getPaymentId());
    }

    @Test
    void deletePayment() {
        // given
        paymentService.removePayment(1L);

        // when
        when(paymentRepository.removePayment(anyLong())).thenReturn(true);

        // then
        assertTrue(paymentService.removePayment(1L));
    }

    private void initPayments() {
        map.put(1L, new PaymentModel(1L, 1_000.0, "First record"));
        map.put(2L, new PaymentModel(2L, 500.0, "Second record"));
        map.put(3L, new PaymentModel(3L, 1_500.0, "Third record"));
    }
}
