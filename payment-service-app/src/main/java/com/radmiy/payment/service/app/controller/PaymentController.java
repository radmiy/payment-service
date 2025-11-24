package com.radmiy.payment.service.app.controller;

import com.radmiy.payment.service.app.exception.PaymentNotFoundException;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentDto> getAllPayments() {
        return Optional.ofNullable(paymentService.getPayments())
                .orElseThrow(() -> new PaymentNotFoundException("No payments found"));
    }

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable int id) {
        return Optional.ofNullable(paymentService.getPayment(id))
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    @PostMapping
    public PaymentDto createPayment(@RequestBody PaymentDto paymentDto) {
        return Optional.ofNullable(paymentService.addPayment(paymentDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Payment already exists"));
    }

    @DeleteMapping("/{id}")
    public boolean deletePayment(@PathVariable int id) {
        return paymentService.removePayment(id);
    }
}
