package com.radmiy.xpayment.adapter.app.config.api;

import com.radmiy.xpayment.adapter.app.api.ApiClient;
import com.radmiy.xpayment.adapter.app.api.client.DefaultApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
public class XPaymentApiConfiguration {

    private final XPaymentApiProperties properties;

    @Bean
    public RestTemplate xPaymentRestTemplate() {
        RestTemplate template = new RestTemplate();
        template.getInterceptors().add((req, body, ex) -> {
            req.getHeaders().setBasicAuth(properties.getUserName(), properties.getPassword());
            req.getHeaders().add("X-Pay-Account", properties.getAccount());
            return ex.execute(req, body);
        });
        return template;
    }

    @Bean
    public ApiClient xPaymentApiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(properties.getUrl());
        return apiClient;
    }

    @Bean
    public DefaultApi defaultApi(ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
