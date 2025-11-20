package com.radmiy.payment.service.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.radmiy.payment.service.app"})
public class PaymentServiceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceAppApplication.class, args);
    }

}
