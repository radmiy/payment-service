package com.radmiy.payment.service.app.repository;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing and modifying payment data.
 * Defines low-level CRUD operations for persistence layer.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByStatus(PaymentStatus status);
}
