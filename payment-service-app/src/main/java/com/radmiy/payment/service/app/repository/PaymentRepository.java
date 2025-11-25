package com.radmiy.payment.service.app.repository;

import com.radmiy.payment.service.app.model.PaymentModel;

import java.util.List;

/**
 * Repository interface for accessing and modifying payment data.
 * Defines low-level CRUD operations for persistence layer.
 */
public interface PaymentRepository {

    /**
     * Returns all stored payments.
     *
     * @return a list of all payment models
     */
    List<PaymentModel> getPayments();

    /**
     * Returns a payment by its identifier.
     *
     * @param id the unique identifier of the payment
     * @return the payment model, or {@code null} if no payment with such id exists
     */
    PaymentModel getPayment(long id);

    /**
     * Persists a new payment in the storage.
     *
     * @param model the payment model to persist; must not be {@code null}
     * @return the persisted payment model including generated identifiers or metadata
     */
    PaymentModel addPayment(PaymentModel model);

    /**
     * Deletes a payment by its identifier.
     *
     * @param id the unique identifier of the payment to delete
     * @return {@code true} if the payment was deleted successfully,
     *         {@code false} if no payment with the given id exists
     */
    boolean removePayment(long id);
}
