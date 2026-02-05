package com.radmiy.xpayment.adapter.app.checkstatus;

import com.radmiy.xpayment.adapter.app.checkstatus.dto.PaymentStatusCheckMessage;

public interface PaymentStatusCheckProducer {

    void send(PaymentStatusCheckMessage message);
}
