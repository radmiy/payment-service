package com.radmiy.payment.service.api.dto;

import com.radmiy.payment.service.api.Message;
import com.radmiy.payment.service.api.XPaymentAdapterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payment message
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class XPaymentAdapterResponseMessage implements Message {

    /**
     * Unique message id
     */
    private UUID messageGuid;

    /**
     * Unique payment id
     */
    private UUID paymentGuid;

    /**
     * Payment amount
     */
    private BigDecimal amount;

    /**
     * Payment Currency
     */
    private String currency;

    /**
     * Unique transaction id
     */
    private UUID transactionRefId;

    /**
     * Payment status
     */
    private XPaymentAdapterStatus status;

    /**
     * Date of payment
     */
    private OffsetDateTime occurredAt;

    @Override
    public UUID getMessageId() {
        return messageGuid;
    }

    @Override
    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }
}
