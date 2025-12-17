package com.radmiy.payment.service.app.controller;

import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .body(paymentService.getPayment(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PaymentDto>> getPaymentsByStatus(
            @ModelAttribute PaymentFilter filter,
            @PageableDefault(page = 0, size = 25, sort = "amount", direction = DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(paymentService.searchPaged(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok()
                .body(paymentService.addPayment(paymentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePayment(@PathVariable UUID id) {
        paymentService.removePayment(id);
        return ResponseEntity.ok().body(true);
    }
}
