package com.radmiy.payment.service.app.controller;

import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {

        return ResponseEntity.ok()
                .body(paymentService.getPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(paymentService.getPayment(id));
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok()
                .body(paymentService.addPayment(paymentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePayment(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(paymentService.removePayment(id));
    }
}
