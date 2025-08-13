package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.SmsNotificationConfigController;
import org.soa.companyService.dto.CreateSmsNotificationConfigRequest;
import org.soa.companyService.dto.UpdateSmsNotificationConfigRequest;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SmsNotificationConfigController.class)
class SmsNotificationConfigControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private SmsNotificationConfigService service;

    private SmsNotificationConfig sample(Long id, String name) {
        SmsNotificationConfig c = new SmsNotificationConfig();
        c.setId(id);
        c.setName(name);
        return c;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(service.getAllSmsNotificationConfigs()).thenReturn(List.of(sample(1L, "Default")));
        mvc.perform(get("/api/sms-notification-configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Default")));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(service.getSmsNotificationConfigById(9L)).thenReturn(Optional.of(sample(9L, "X")));
        mvc.perform(get("/api/sms-notification-configs/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)));
    }

    @Test
    void post_create_ok() throws Exception {
        CreateSmsNotificationConfigRequest req = new CreateSmsNotificationConfigRequest();
        req.setName("Promo");

        SmsNotificationConfig saved = sample(100L, "Promo");
        Mockito.when(service.createSmsNotificationConfig(any(SmsNotificationConfig.class))).thenReturn(saved);

        mvc.perform(post("/api/sms-notification-configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Promo")));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateSmsNotificationConfigRequest req = new UpdateSmsNotificationConfigRequest();
        req.setName("NewName");

        SmsNotificationConfig updated = sample(200L, "NewName");
        Mockito.when(service.updateSmsNotificationConfig(eq(200L), any(SmsNotificationConfig.class))).thenReturn(updated);

        mvc.perform(put("/api/sms-notification-configs/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/sms-notification-configs/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(service).deleteSmsNotificationConfig(300L);
    }
}
