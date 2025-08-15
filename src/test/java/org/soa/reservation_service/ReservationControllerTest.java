package org.soa.reservation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.reservation_service.controller.ReservationController;
import org.soa.reservation_service.dto.AdminReservationUpdateRequest;
import org.soa.reservation_service.dto.ReservationCreateRequest;
import org.soa.reservation_service.dto.ReservationStatusUpdateRequest;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.service.PaymentService;
import org.soa.reservation_service.service.ReservationService;
import org.soa.reservation_service.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private ReservationService reservationService;
    @MockitoBean private StatusService statusService;   // injected in controller, not used directly in tests
    @MockitoBean private PaymentService paymentService; // injected in controller, not used directly in tests

    private Reservation sample(Long id) {
        Reservation r = new Reservation();
        r.setId(id);
        r.setCustomerFullName("John Doe");
        r.setDate(new Timestamp(System.currentTimeMillis()));
        r.setDateTo(new Timestamp(System.currentTimeMillis() + 3600_000));
        return r;
    }

    @Test
    void getAllByCompany_ok() throws Exception {
        Mockito.when(reservationService.getAllReservationsByCompany(10L)).thenReturn(List.of(sample(1L), sample(2L)));
        mvc.perform(get("/api/reservations/company/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllByUser_ok() throws Exception {
        Mockito.when(reservationService.getAllReservationsByUser(77L)).thenReturn(List.of(sample(5L)));
        mvc.perform(get("/api/reservations/user/77"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(5)));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(reservationService.getReservationById(9L)).thenReturn(Optional.of(sample(9L)));
        mvc.perform(get("/api/reservations/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerFullName", is("John Doe")));
    }

    @Test
    void post_create_ok() throws Exception {
        ReservationCreateRequest req = new ReservationCreateRequest();
        // Minimal fields are fine because service is mocked
        req.setIdCompany(1L);
        req.setIdService(2L);
        req.setPaymentId(3L);
        req.setCustomerFullName("Alice");
        req.setDate(null); // avoid Timestamp parsing; service is mocked

        Reservation created = sample(100L);
        created.setCustomerFullName("Alice");
        Mockito.when(reservationService.createReservation(any(ReservationCreateRequest.class))).thenReturn(created);

        mvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.customerFullName", is("Alice")));
    }

    @Test
    void getFreeSlots_ok() throws Exception {
        List<String> slots = List.of("10:00 - 10:30", "10:30 - 11:00");
        Mockito.when(reservationService.getFreeSlots(eq(55L), any(LocalDateTime.class), eq(2L), eq(3L)))
                .thenReturn(slots);

        // Call the updated GET-with-query-params endpoint (employeeId is optional)
        mvc.perform(get("/api/reservations/free-slots/55")
                        .param("date", "2025-01-01T10:00:00")
                        .param("serviceId", "2")
                        .param("employeeId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("10:00 - 10:30")));
    }

    @Test
    void put_confirm_ok() throws Exception {
        Mockito.when(reservationService.confirmReservation(7L, 123456L)).thenReturn(sample(7L));
        mvc.perform(put("/api/reservations/7/confirm/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)));
    }

    @Test
    void put_status_ok() throws Exception {
        ReservationStatusUpdateRequest req = new ReservationStatusUpdateRequest();
        req.setStatusId(9L);

        Mockito.when(reservationService.updateReservationStatus(8L, 9L)).thenReturn(sample(8L));

        mvc.perform(put("/api/reservations/8/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(8)));
    }

    @Test
    void put_adminUpdate_ok() throws Exception {
        AdminReservationUpdateRequest req = new AdminReservationUpdateRequest();
        req.setStatusId(4L);

        Mockito.when(reservationService.adminUpdateReservation(12L, 4L)).thenReturn(sample(12L));

        mvc.perform(put("/api/reservations/admin/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(12)));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/reservations/99"))
                .andExpect(status().isNoContent());
        Mockito.verify(reservationService).cancelReservation(99L);
    }
    @Test
    void getAllByEmployee_ok() throws Exception {
        // given
        Mockito.when(reservationService.getReservationsByEmployee(321L))
                .thenReturn(List.of(sample(11L), sample(12L)));

        // when/then
        mvc.perform(get("/api/reservations")
                        .param("employee_id", "321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(11)))
                .andExpect(jsonPath("$[1].id", is(12)));

        Mockito.verify(reservationService).getReservationsByEmployee(321L);
    }
}
