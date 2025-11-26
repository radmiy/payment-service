package com.radmiy.payment.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("MagicNumber")
public class PaymentModel {

    private Long paymentId;
    private Double value;
    private String name;
}
