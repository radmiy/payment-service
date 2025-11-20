package com.radmiy.payment.service.app.repository;

import com.radmiy.payment.service.app.model.PaymentModel;

import java.util.List;

public interface Repository {

    List<PaymentModel> getPayments();

    PaymentModel getPayment(long id);

    void addPayment(PaymentModel model);

    void removePayment(long id);
}
