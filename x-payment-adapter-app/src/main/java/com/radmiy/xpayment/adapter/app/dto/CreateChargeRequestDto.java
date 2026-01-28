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
public class CreateChargeRequestDto {

    private BigDecimal amount;
    private String currency;
    private String customer;
    private UUID order;
    private String receiptEmail;
}
