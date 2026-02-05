package com.radmiy.xpayment.adapter.app.checkstatus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentStatusCheckMessage {

    private UUID chargeGuid;
    private UUID paymentGuid;
    private BigDecimal amount;
    private String currency;

    public PaymentStatusCheckMessage clone() {
        return new PaymentStatusCheckMessage(chargeGuid, paymentGuid, amount, currency);
    }
}
