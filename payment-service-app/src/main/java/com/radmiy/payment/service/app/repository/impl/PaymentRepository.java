package com.radmiy.payment.service.app.repository.impl;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.repository.Repository;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class PaymentRepository implements Repository {

    private final Map<Long, PaymentModel> map = new HashMap<>();

    {
        map.put(1L, new PaymentModel(1L, 1_000, "First record"));
        map.put(2L, new PaymentModel(2L, 500, "Second record"));
        map.put(3L, new PaymentModel(3L, 1_500, "Third record"));
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
    public void addPayment(PaymentModel model) {
        long id = (long) (Math.random() * 1_000_000);
        model.setPaymentId(id);
        map.put(id, model);
    }

    @Override
    public void removePayment(long id) {
        map.remove(id);
    }

}
