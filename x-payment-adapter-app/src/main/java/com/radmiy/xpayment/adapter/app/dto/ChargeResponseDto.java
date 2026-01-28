package com.radmiy.xpayment.adapter.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChargeResponseDto {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private BigDecimal amountReceived;
    private String createdAt;
    private String chargedAt;
    private String customer;
    private UUID order;
    private String receiptEmail;
    private String status;
}
