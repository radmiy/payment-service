package com.radmiy.payment.service.app.repository.impl;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Map<Long, PaymentModel> map = new HashMap<>();

    public PaymentRepositoryImpl() {
        initPayments();
    }

    private void initPayments() {
        final int value = 1_000_000;
        Long index = 0L;
        map.put(index, new PaymentModel(index++, Math.random() * value, "First record"));
        map.put(index, new PaymentModel(index++, Math.random() * value, "Second record"));
        map.put(index, new PaymentModel(index, Math.random() * value, "Third record"));
    }

    @Override
    public List<PaymentModel> getPayments() {
        return new ArrayList<>(map.values());
    }

    @Override
    public PaymentModel getPayment(long id) {
        return map.get(id);
    }

    @Override
    public PaymentModel addPayment(PaymentModel payment) {
        final Long id = (long) (Math.random() * 1_000_000);
        payment.setPaymentId(id);
        if (map.containsKey(id)) {
            return null;
        }
        map.put(id, payment);
        return payment;
    }

    @Override
    public boolean removePayment(long id) {
        return map.remove(id) != null;
    }

}
