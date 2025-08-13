package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.CompanyController;
import org.soa.companyService.dto.CreateCompanyRequest;
import org.soa.companyService.dto.UpdateCompanyRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.Location;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.LocationService;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private CompanyService companyService;
    @MockitoBean private SmsNotificationConfigService smsService;
    @MockitoBean private LocationService locationService;

    private Company sampleCompany(Long id) {
        Company c = new Company();
        c.setId(id);
        c.setCompanyName("Acme");
        return c;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(companyService.getAllCompanies()).thenReturn(List.of(sampleCompany(1L)));
        mvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(companyService.getCompanyById(7L)).thenReturn(Optional.of(sampleCompany(7L)));
        mvc.perform(get("/api/companies/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName", is("Acme")));
    }

    @Test
    void post_create_ok() throws Exception {
        CreateCompanyRequest req = new CreateCompanyRequest();
        req.setCompanyName("Acme");
        req.setAddress("Street 1");
        req.setEmail("a@b.com");
        req.setPhoneNumber("123");
        req.setLocationId(55L);
        req.setSmsNotificationConfigId(66L);

        Location loc = new Location(); loc.setId(55L);
        SmsNotificationConfig snc = new SmsNotificationConfig(); snc.setId(66L);

        Mockito.when(locationService.getLocationById(55L)).thenReturn(Optional.of(loc));
        Mockito.when(smsService.getSmsNotificationConfigById(66L)).thenReturn(Optional.of(snc));

        Company saved = sampleCompany(100L);
        Mockito.when(companyService.createCompany(any(Company.class))).thenReturn(saved);

        mvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateCompanyRequest req = new UpdateCompanyRequest();
        req.setCompanyName("NewName");
        req.setAddress("New St");
        req.setLocationId(55L);
        req.setSmsNotificationConfigId(66L);

        Location loc = new Location(); loc.setId(55L);
        SmsNotificationConfig snc = new SmsNotificationConfig(); snc.setId(66L);
        Mockito.when(locationService.getLocationById(55L)).thenReturn(Optional.of(loc));
        Mockito.when(smsService.getSmsNotificationConfigById(66L)).thenReturn(Optional.of(snc));

        Company updated = sampleCompany(200L);
        updated.setCompanyName("NewName");
        Mockito.when(companyService.updateCompany(eq(200L), any(Company.class))).thenReturn(updated);

        mvc.perform(put("/api/companies/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName", is("NewName")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/companies/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(companyService).deleteCompany(300L);
    }

    @Test
    void uploadPicture_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", new byte[]{1,2,3});
        mvc.perform(multipart("/api/companies/{id}/upload-picture", 10L).file(file))
                .andExpect(status().isOk());
        Mockito.verify(companyService).uploadPicture(10L, file);
    }
}
