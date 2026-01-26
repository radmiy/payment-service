package com.radmiy.xpayment.adapter.app.config.api;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "x-payment-api.client")
public class XPaymentApiProperties {

    private String url;
    private String userName;
    private String password;
    private String account;
}
