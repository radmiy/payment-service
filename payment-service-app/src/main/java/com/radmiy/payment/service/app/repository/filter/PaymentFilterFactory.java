package com.radmiy.payment.service.app.repository.filter;

import com.radmiy.payment.service.app.model.Payment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class PaymentFilterFactory {

    public static Specification<Payment> fromFilter(PaymentFilter
                                                            filter) {
        Specification<Payment> spec = Specification.unrestricted();

        if (StringUtils.hasText(filter.getCurrency())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("currency"), filter.getCurrency()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getMinAmount() != null && filter.getMaxAmount() != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("amount"), filter.getMinAmount(),
                    filter.getMaxAmount()));
        }

        if (filter.getCreatedAfter() != null &&
                filter.getCreatedBefore() != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"),
                    filter.getCreatedAfter(), filter.getCreatedBefore()));
        }

        return spec;
    }
}
