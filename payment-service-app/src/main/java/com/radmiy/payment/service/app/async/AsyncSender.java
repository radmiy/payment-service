package com.radmiy.payment.service.app.async;

/**
 * Interface for async message processing
 * @param <T> Type of message
 */
public interface AsyncSender<T extends Message> {

    /**
     * Send message
     * @param message
     */
    void send(T message);
}
