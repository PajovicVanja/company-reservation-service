package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.BusinessHoursController;
import org.soa.companyService.dto.CreateBusinessHoursRequest;
import org.soa.companyService.dto.UpdateBusinessHoursRequest;
import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.model.Company;
import org.soa.companyService.service.BusinessHoursService;
import org.soa.companyService.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessHoursController.class)
class BusinessHoursControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private BusinessHoursService businessHoursService;
    @MockitoBean private CompanyService companyService;

    private BusinessHours sampleBH(Long id) {
        BusinessHours bh = new BusinessHours();
        bh.setId(id);
        bh.setDayNumber(1);
        bh.setTimeFrom(Time.valueOf("09:00:00"));
        bh.setTimeTo(Time.valueOf("17:00:00"));
        bh.setDay("MONDAY");
        Company c = new Company();
        c.setId(11L);
        bh.setCompany(c);
        return bh;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(businessHoursService.getAllBusinessHours()).thenReturn(List.of(sampleBH(1L)));
        mvc.perform(get("/api/business-hours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(businessHoursService.getBusinessHoursById(5L)).thenReturn(Optional.of(sampleBH(5L)));
        mvc.perform(get("/api/business-hours/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void post_create_ok() throws Exception {
        CreateBusinessHoursRequest req = new CreateBusinessHoursRequest();
        req.setDayNumber(2);
        req.setTimeFrom(Time.valueOf("08:00:00"));
        req.setTimeTo(Time.valueOf("16:00:00"));
        req.setDay("TUESDAY");
        req.setCompanyId(11L);

        Company company = new Company();
        company.setId(11L);
        Mockito.when(companyService.getCompanyById(11L)).thenReturn(Optional.of(company));

        BusinessHours saved = sampleBH(100L);
        saved.setDayNumber(2);
        saved.setDay("TUESDAY");
        Mockito.when(businessHoursService.createBusinessHours(any(BusinessHours.class))).thenReturn(saved);

        mvc.perform(post("/api/business-hours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.day", is("TUESDAY")));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateBusinessHoursRequest req = new UpdateBusinessHoursRequest();
        req.setDayNumber(3);
        req.setTimeFrom(Time.valueOf("10:00:00"));
        req.setTimeTo(Time.valueOf("18:00:00"));
        req.setDay("WEDNESDAY");
        req.setCompanyId(11L);

        Company company = new Company();
        company.setId(11L);
        Mockito.when(companyService.getCompanyById(11L)).thenReturn(Optional.of(company));

        BusinessHours updated = sampleBH(200L);
        updated.setDayNumber(3);
        updated.setDay("WEDNESDAY");
        Mockito.when(businessHoursService.updateBusinessHours(eq(200L), any(BusinessHours.class))).thenReturn(updated);

        mvc.perform(put("/api/business-hours/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayNumber", is(3)))
                .andExpect(jsonPath("$.day", is("WEDNESDAY")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/business-hours/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(businessHoursService).deleteBusinessHours(300L);
    }
}
