package com.radmiy.payment.service.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radmiy.payment.service.app.AbstractPostgresIntegrationTest;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class PaymentControllerTestIT extends AbstractPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER", "READER"})
    void shouldReturnedOnlyLiquibasePayments() throws Exception {
        mockMvc.perform(get("/payments")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[*].guid", containsInAnyOrder(
                        "550e8400-e29b-41d4-a716-446655440001",
                        "550e8400-e29b-41d4-a716-446655440002",
                        "550e8400-e29b-41d4-a716-446655440003",
                        "550e8400-e29b-41d4-a716-446655440004",
                        "550e8400-e29b-41d4-a716-446655440005",
                        "550e8400-e29b-41d4-a716-446655440006",
                        "550e8400-e29b-41d4-a716-446655440007",
                        "550e8400-e29b-41d4-a716-446655440008",
                        "550e8400-e29b-41d4-a716-446655440009",
                        "550e8400-e29b-41d4-a716-446655440010"
                )));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER", "READER"})
    void shouldReturnedOnlyOnePayment() throws Exception {
        mockMvc.perform(get("/payments/550e8400-e29b-41d4-a716-446655440001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid").value(
                        "550e8400-e29b-41d4-a716-446655440001"
                ));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER", "READER"})
    void shouldReturnedErrorPaymentDoesNotExistTest() throws Exception {
        mockMvc.perform(get("/payments/550e8400-e29b-41d4-a716-446655440000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void shouldCreatePaymentSuccessfullyTest() throws Exception {
        Map<String, Object> paymentPayload = Map.of(
                "inquiry_ref_id", "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d",
                "amount", 150.00,
                "currency", "USD",
                "status", "PENDING",
                "note", "Тестовый платеж от 2026 года"
        );

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid").exists())
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdatedPaymentSuccessfullyTest() throws Exception {
        Map<String, Object> paymentPayload = Map.of(
                "inquiry_ref_id", "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d",
                "amount", 150.00,
                "currency", "USD",
                "status", "PENDING",
                "note", "Тестовый платеж от 2026 года"
        );

        mockMvc.perform(put("/payments/550e8400-e29b-41d4-a716-446655440008")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid")
                        .value("550e8400-e29b-41d4-a716-446655440008"))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSendNotFoundErrorWhileUpdatedPaymentTest() throws Exception {
        Map<String, Object> paymentPayload = Map.of(
                "inquiry_ref_id", "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d",
                "amount", 150.00,
                "currency", "USD",
                "status", "PENDING",
                "note", "Тестовый платеж от 2026 года"
        );

        mockMvc.perform(put("/payments/550e8400-e29b-41d4-a716-446655440000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeletePaymentTest() throws Exception {
        mockMvc.perform(delete("/payments/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSendNotFoundErrorWhileDeletePaymentTest() throws Exception {
        mockMvc.perform(delete("/payments/550e8400-e29b-41d4-a716-446655440000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateStatusSuccessfullyTest() throws Exception {
        Map<String, Object> statusPayload = Map.of(
                "status", "PENDING"
        );

        mockMvc.perform(patch("/payments/550e8400-e29b-41d4-a716-446655440001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSendNotFoundErrorWhileUpdateStatusTest() throws Exception {
        Map<String, Object> statusPayload = Map.of(
                "status", "PENDING"
        );

        mockMvc.perform(patch("/payments/550e8400-e29b-41d4-a716-446655440000/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnedPaymentWithApprovedStatusTest() throws Exception {
        PaymentFilter paymentFilter = PaymentFilter.builder()
                .status(PaymentStatus.PENDING)
                .build();

        mockMvc.perform(get("/payments/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "APPROVED")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[*].guid", containsInAnyOrder(
                        "550e8400-e29b-41d4-a716-446655440001",
                        "550e8400-e29b-41d4-a716-446655440006",
                        "550e8400-e29b-41d4-a716-446655440008"
                )));
    }
}
