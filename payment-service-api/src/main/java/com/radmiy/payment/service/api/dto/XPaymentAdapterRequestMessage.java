package com.radmiy.payment.service.api.dto;

import com.radmiy.payment.service.api.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


/**
 * Request payment message
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class XPaymentAdapterRequestMessage implements Message {

    private UUID paymentGuid;

    /**
     * Payment amount
     */
    private BigDecimal amount;

    /**
     * Payment currency
     */
    private String currency;

    /**
     * Date of payment
     */
    private OffsetDateTime occurredAt;

    @Override
    public UUID getMessageId() {
        return paymentGuid;
    }

    @Override
    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }
}
