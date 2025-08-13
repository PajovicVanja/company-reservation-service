package org.soa.reservation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.reservation_service.controller.PaymentController;
import org.soa.reservation_service.dto.PaymentCreateRequest;
import org.soa.reservation_service.dto.PaymentUpdateRequest;
import org.soa.reservation_service.model.Payment;
import org.soa.reservation_service.model.PaymentTypes;
import org.soa.reservation_service.service.PaymentService;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private PaymentService paymentService;
    @MockitoBean private PaymentTypesService paymentTypesService;

    private Payment sample(Long id) {
        Payment p = new Payment();
        p.setId(id);
        p.setStatus("PAID");
        p.setAmount(new BigDecimal("99.50"));
        p.setDatePaid(new Timestamp(System.currentTimeMillis()));
        return p;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(paymentService.getAllPayments()).thenReturn(List.of(sample(1L)));
        mvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(paymentService.getPaymentById(7L)).thenReturn(Optional.of(sample(7L)));
        mvc.perform(get("/api/payments/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    @Test
    void post_create_ok() throws Exception {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setPaymentTypeId(5L);
        req.setStatus("PENDING");
        req.setAmount(new BigDecimal("10.00"));
        req.setDatePaid(null);

        PaymentTypes type = new PaymentTypes();
        type.setId(5L);
        type.setName("CARD");
        Mockito.when(paymentTypesService.getPaymentTypeById(5L)).thenReturn(Optional.of(type));

        Payment saved = sample(100L);
        saved.setStatus("PENDING");
        saved.setPaymentTypes(type);
        saved.setAmount(new BigDecimal("10.00"));
        Mockito.when(paymentService.createPayment(any(Payment.class))).thenReturn(saved);

        mvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void put_update_ok() throws Exception {
        PaymentUpdateRequest req = new PaymentUpdateRequest();
        req.setPaymentTypeId(6L);
        req.setStatus("REFUNDED");
        req.setAmount(new BigDecimal("0.00"));

        PaymentTypes type = new PaymentTypes();
        type.setId(6L);
        type.setName("CASH");
        Mockito.when(paymentTypesService.getPaymentTypeById(6L)).thenReturn(Optional.of(type));

        Payment updated = sample(200L);
        updated.setStatus("REFUNDED");
        updated.setPaymentTypes(type);
        updated.setAmount(new BigDecimal("0.00"));
        Mockito.when(paymentService.updatePayment(eq(200L), any(Payment.class))).thenReturn(updated);

        mvc.perform(put("/api/payments/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("REFUNDED")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/payments/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(paymentService).deletePayment(300L);
    }
}
