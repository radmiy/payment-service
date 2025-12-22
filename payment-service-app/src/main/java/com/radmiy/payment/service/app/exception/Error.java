package com.radmiy.payment.service.app.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error {

    NULL_ID(HttpStatus.BAD_REQUEST, "ID cannot be null"),
    NEGATIVE_ID(HttpStatus.BAD_REQUEST, "ID cannot be 0 or negative"),
    PAYMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "Payment id=%s does not exist"),
    PAYMENT_IS_NUL(HttpStatus.BAD_REQUEST, "Payment cannot be null"),
    PAYMENT_EXIST(HttpStatus.BAD_REQUEST, "Payment exists");

    private final HttpStatus status;
    private final String message;
}
