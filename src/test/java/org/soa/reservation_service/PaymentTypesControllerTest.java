package org.soa.reservation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.reservation_service.controller.PaymentTypesController;
import org.soa.reservation_service.dto.PaymentTypesCreateRequest;
import org.soa.reservation_service.dto.PaymentTypesUpdateRequest;
import org.soa.reservation_service.model.PaymentTypes;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(PaymentTypesController.class)
class PaymentTypesControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private PaymentTypesService paymentTypesService;

    private PaymentTypes sample(Long id) {
        PaymentTypes t = new PaymentTypes();
        t.setId(id);
        t.setName("CARD");
        t.setDurationFrom(new Timestamp(System.currentTimeMillis()));
        t.setDurationTo(new Timestamp(System.currentTimeMillis() + 1000));
        t.setAddedBy(1L);
        return t;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(paymentTypesService.getAllPaymentTypes()).thenReturn(List.of(sample(1L)));
        mvc.perform(get("/api/payment-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("CARD")));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(paymentTypesService.getPaymentTypeById(9L)).thenReturn(Optional.of(sample(9L)));
        mvc.perform(get("/api/payment-types/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)));
    }

    @Test
    void post_create_ok() throws Exception {
        PaymentTypesCreateRequest req = new PaymentTypesCreateRequest();
        req.setName("CASH");

        PaymentTypes saved = sample(100L);
        saved.setName("CASH");
        Mockito.when(paymentTypesService.createPaymentType(any(PaymentTypes.class))).thenReturn(saved);

        mvc.perform(post("/api/payment-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("CASH")));
    }

    @Test
    void put_update_ok() throws Exception {
        PaymentTypesUpdateRequest req = new PaymentTypesUpdateRequest();
        req.setName("WIRE");

        PaymentTypes updated = sample(200L);
        updated.setName("WIRE");
        Mockito.when(paymentTypesService.updatePaymentType(eq(200L), any(PaymentTypes.class))).thenReturn(updated);

        mvc.perform(put("/api/payment-types/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("WIRE")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/payment-types/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(paymentTypesService).deletePaymentType(300L);
    }
}
