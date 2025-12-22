package com.radmiy.payment.service.app.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentNoteUpdateDto {

    @NotNull
    private String note;

}
