package org.soa.reservation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.reservation_service.controller.StatusController;
import org.soa.reservation_service.dto.StatusCreateRequest;
import org.soa.reservation_service.dto.StatusUpdateRequest;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(StatusController.class)
class StatusControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private StatusService statusService;

    private Status sample(Long id, String name) {
        Status s = new Status();
        s.setId(id);
        s.setName(name);
        s.setAddedBy(1L);
        return s;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(statusService.getAllStatuses()).thenReturn(List.of(sample(1L, "New")));
        mvc.perform(get("/api/statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("New")));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(statusService.getStatusById(9L)).thenReturn(Optional.of(sample(9L, "X")));
        mvc.perform(get("/api/statuses/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)));
    }

    @Test
    void post_create_ok() throws Exception {
        StatusCreateRequest req = new StatusCreateRequest();
        req.setName("Pending");
        req.setAddedBy(2L);

        Status saved = sample(100L, "Pending");
        Mockito.when(statusService.createStatus(any(Status.class))).thenReturn(saved);

        mvc.perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Pending")));
    }

    @Test
    void put_update_ok() throws Exception {
        StatusUpdateRequest req = new StatusUpdateRequest();
        req.setName("Done");
        req.setAddedBy(3L);

        Status updated = sample(200L, "Done");
        Mockito.when(statusService.updateStatus(eq(200L), any(Status.class))).thenReturn(updated);

        mvc.perform(put("/api/statuses/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Done")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/statuses/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(statusService).deleteStatus(300L);
    }
}
