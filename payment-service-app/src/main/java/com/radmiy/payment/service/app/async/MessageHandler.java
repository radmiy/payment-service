package com.radmiy.payment.service.app.async;

/**
 * Processing message Interface
 * @param <T> message type
 */
public interface MessageHandler<T extends Message> {

    /**
     * Processing message
     * @param message
     */
    void handle(T message);
}
