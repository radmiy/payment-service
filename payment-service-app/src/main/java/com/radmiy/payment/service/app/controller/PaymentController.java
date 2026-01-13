package com.radmiy.payment.service.app.controller;

import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.model.dto.PaymentNoteUpdateDto;
import com.radmiy.payment.service.app.model.dto.PaymentStatusUpdateDto;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        log.info("Create payment {}", paymentDto);
        return ResponseEntity.ok()
                .body(paymentService.createPayment(paymentDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'READER')")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID id) {
        log.info("Get payment by id: {}", id);
        return ResponseEntity.ok()
                .body(paymentService.getPayment(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable UUID id, @RequestBody PaymentDto dto) {
        log.info("Update payment by id: {}", id);
        return ResponseEntity.ok().body(paymentService.updatePayment(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        log.info("Delete payment by id: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'READER')")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        log.info("Get all payments");
        return ResponseEntity.ok()
                .body(paymentService.getPayments());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'READER')")
    public ResponseEntity<Page<PaymentDto>> getPaymentsByStatus(
            @ModelAttribute PaymentFilter filter,
            @PageableDefault(page = 0, size = 25, sort = "amount", direction = DESC)
            Pageable pageable
    ) {
        log.info("Search payment by {}", filter);
        return ResponseEntity.ok().body(paymentService.searchPaged(filter, pageable));
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid PaymentStatusUpdateDto dto
    ) {
        log.info("Update payment status by id: {}", id);
        return ResponseEntity.ok().body(paymentService.updateStatus(id, dto.getStatus()));
    }

    @PatchMapping("/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> updateNote(
            @PathVariable UUID id,
            @RequestBody @Valid PaymentNoteUpdateDto dto
    ) {
        log.info("Update payment note by id: {}", id);
        return ResponseEntity.ok().body(paymentService.updateNote(id, dto.getNote()));
    }
}
