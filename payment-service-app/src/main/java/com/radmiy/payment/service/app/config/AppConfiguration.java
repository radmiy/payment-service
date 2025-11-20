package com.radmiy.payment.service.app.config;

import com.radmiy.payment.service.app.repository.Repository;
import com.radmiy.payment.service.app.repository.impl.PaymentRepository;
import com.radmiy.payment.service.app.service.Service;
import com.radmiy.payment.service.app.service.impl.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public Repository paymentRepository() {
        return new PaymentRepository();
    }

    @Bean
    public Service paymentService(Repository paymentRepository) {
        return new PaymentService(paymentRepository);
    }
}
