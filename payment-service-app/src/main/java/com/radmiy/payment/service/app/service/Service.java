package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.dto.PaymentDto;

import java.util.List;

public interface Service {

    PaymentDto getPayment(long id);

    List<PaymentDto> getPayments();

    void addPayment(PaymentDto paymentDto);

    void removePayment(long id);
}
