package com.radmiy.payment.service.app.mapper;

import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.radmiy.payment.service.app.model.PaymentStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentMapperTest {

    @Spy
    private final PaymentMapper delegate = new PaymentMapperImpl_();

    @InjectMocks
    private final PaymentMapperImpl mapper = new PaymentMapperImpl();

    @Test
    void toDto() {
        // given
        UUID id = UUID.randomUUID();
        Payment payment = Payment.builder()
                .guid(id)
                .amount(new BigDecimal("123.45"))
                .currency("USD")
                .inquiryRefId(UUID.randomUUID())
                .status(APPROVED)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        BigDecimal expectedConverted = payment.getAmount().multiply(new BigDecimal(3));

        // when
        PaymentDto dto = mapper.toDto(payment);

        // then
        assertNotNull(dto);
        assertEquals(payment.getGuid(), dto.getGuid());
        assertEquals(payment.getAmount(), dto.getAmount());
        assertEquals(payment.getCurrency(), dto.getCurrency());
        assertEquals(payment.getStatus(), dto.getStatus());
        assertEquals(payment.getCreatedAt(), dto.getCreatedAt());
        assertEquals(payment.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(expectedConverted, dto.getConverted());
    }

    @Test
    void toEntity() {
        // given
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        PaymentDto dto = PaymentDto.builder()
                .guid(id)
                .amount(new BigDecimal("999.99"))
                .currency("EUR")
                .status(PaymentStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        Payment actual = mapper.toEntity(dto);

        // then
        assertNotNull(actual);
        assertEquals(dto.getGuid(), actual.getGuid());
        assertEquals(dto.getAmount(), actual.getAmount());
        assertEquals(dto.getCurrency(), actual.getCurrency());
        assertEquals(dto.getStatus(), actual.getStatus());
        assertEquals(dto.getCreatedAt(), actual.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), actual.getUpdatedAt());
    }
}