package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.app.model.dto.PaymentDto;

import java.util.List;

/**
 * Service layer for managing payments.
 * Provides business operations for retrieving, creating and deleting payments.
 */
public interface PaymentService {

    /**
     * Returns a payment by its identifier.
     *
     * @param id the unique identifier of the payment
     * @return the payment DTO, or {@code null} if the payment does not exist
     */
    PaymentDto getPayment(long id);

    /**
     * Returns all available payments.
     *
     * @return a list of all payment DTOs
     */
    List<PaymentDto> getPayments();

    /**
     * Creates a new payment.
     *
     * @param paymentDto the payment data to create; must not be {@code null}
     * @return the created payment DTO including generated identifiers or metadata
     */
    PaymentDto addPayment(PaymentDto paymentDto);

    /**
     * Removes a payment by its identifier.
     *
     * @param id the unique identifier of the payment to remove
     * @return {@code true} if the payment was removed successfully,
     *         {@code false} if the payment with the given id was not found
     */
    boolean removePayment(long id);
}
