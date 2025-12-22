package com.radmiy.payment.service.app.model.dto;

import com.radmiy.payment.service.app.model.PaymentStatus;
import  jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentStatusUpdateDto {

    @NotNull
    private PaymentStatus status;

}
