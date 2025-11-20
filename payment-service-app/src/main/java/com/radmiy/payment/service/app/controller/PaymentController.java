package com.radmiy.payment.service.app.controller;

import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.service.Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final Service paymentService;

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable int id) {
        return paymentService.getPayment(id);
    }

    @GetMapping("/all")
    public List<PaymentDto> getAllPayments() {
        return paymentService.getPayments();
    }

    @PostMapping
    public void createPayment(@RequestBody PaymentDto paymentDto) {
        paymentService.addPayment(paymentDto);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable int id) {
        paymentService.removePayment(id);
    }
}
