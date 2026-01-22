package com.radmiy.payment.service.api;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Message Interface with id and date
 */
public interface Message {

    /**
     * Return unique id
     *
     * @return message UUID
     */
    UUID getMessageId();

    /**
     * Return date and time message occurred
     *
     * @return OffsetDateTime message occurred
     */
    OffsetDateTime getOccurredAt();
}