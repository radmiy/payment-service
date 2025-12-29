package com.radmiy.payment.service.app.exception;

import com.radmiy.payment.service.app.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles domain-specific "not found" errors.
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handle(ServiceException ex) {
        final ErrorResponse body = new ErrorResponse(
                1,
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    /**
     * Fallback for any other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        final ErrorResponse body = new ErrorResponse(
                2,
                ex.getMessage()
        );
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(body);
    }
}
