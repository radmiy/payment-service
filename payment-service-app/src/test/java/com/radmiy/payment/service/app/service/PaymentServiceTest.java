package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.impl.PaymentRepository;
import com.radmiy.payment.service.app.service.impl.PaymentService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private final Map<Long, PaymentModel> map = new HashMap<>();

    @InjectMocks
    private PaymentService paymentController;

    @Mock
    private PaymentRepository paymentRepository;


    @Test
    void getPayment() {
        when(paymentRepository.getPayment(anyLong())).thenReturn(map.get(1L));

        PaymentDto model = paymentController.getPayment(1L);

        verify(paymentRepository, times(1)).getPayment(1L);
        assertEquals("First record", model.getName());
        assertEquals(1L, model.getPaymentId());
        assertEquals(1_000, model.getValue());
    }

    @Test
    void getAllPayments() {
        when(paymentRepository.getPayments()).thenReturn(new ArrayList<>(map.values()));

        List<PaymentDto> models = paymentController.getPayments();

        verify(paymentRepository, times(1)).getPayments();
        assertEquals(3, models.size());

    }

    @Test
    void createPayment() {
        paymentController.addPayment(new PaymentDto(1000L, 1000, "Other record"));

        verify(paymentRepository, times(1)).addPayment(any());
    }

    @Test
    void deletePayment() {
        paymentController.removePayment(1L);

        verify(paymentRepository, times(1)).removePayment(anyLong());
    }

    @BeforeEach
    void setUp() {
        initPayments();
    }

    private void initPayments() {
        map.put(1L, new PaymentModel(1L, 1_000, "First record"));
        map.put(2L, new PaymentModel(2L, 500, "Second record"));
        map.put(3L, new PaymentModel(3L, 1_500, "Third record"));
    }
}