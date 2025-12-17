package com.radmiy.payment.service.app.repository.filter;

import com.radmiy.payment.service.app.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentFilter {

    private String currency;
    private PaymentStatus status;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Instant createdAfter;
    private Instant createdBefore;
}
