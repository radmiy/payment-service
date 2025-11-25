package com.radmiy.payment.service.app.repository.impl;

import com.radmiy.payment.service.app.model.PaymentModel;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Map<Long, PaymentModel> map = new HashMap<>();

    {
        map.put(1L, new PaymentModel(1L, 1_000.0, "First record"));
        map.put(2L, new PaymentModel(2L, 500.0, "Second record"));
        map.put(3L, new PaymentModel(3L, 1_500.0, "Third record"));
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
        Long id = (long) (Math.random() * 1_000_000);
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
