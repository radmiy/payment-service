package com.radmiy.payment.service.app.async;

/**
 * Message consumer
 * @param <T> Message type
 */
public interface AsyncListener<T extends Message> {

    /**
     * Send message
     * @param message
     */
    void onMessage(T message);
}
