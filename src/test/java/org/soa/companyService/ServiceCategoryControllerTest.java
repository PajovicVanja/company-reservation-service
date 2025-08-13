package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.ServiceCategoryController;
import org.soa.companyService.dto.CreateServiceCategoryRequest;
import org.soa.companyService.dto.UpdateServiceCategoryRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
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

@WebMvcTest(ServiceCategoryController.class)
class ServiceCategoryControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private ServiceCategoryService serviceCategoryService;
    @MockitoBean private CompanyService companyService;

    private ServiceCategory sample(Long id) {
        ServiceCategory sc = new ServiceCategory();
        sc.setId(id);
        sc.setName("Hair");
        Company c = new Company(); c.setId(10L);
        sc.setCompany(c);
        return sc;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(serviceCategoryService.getAllServiceCategories()).thenReturn(List.of(sample(1L)));
        mvc.perform(get("/api/service-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Hair")));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(serviceCategoryService.getServiceCategoryById(5L)).thenReturn(Optional.of(sample(5L)));
        mvc.perform(get("/api/service-categories/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void getByCompanyUUID_ok() throws Exception {
        Mockito.when(serviceCategoryService.getServiceCategoriesByCompanyUUID("abc-uuid"))
                .thenReturn(List.of(sample(2L)));
        mvc.perform(get("/api/service-categories/company/abc-uuid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(2)));
    }

    @Test
    void post_create_ok() throws Exception {
        CreateServiceCategoryRequest req = new CreateServiceCategoryRequest();
        req.setName("Nails");
        req.setCompanyId(10L);

        Company c = new Company(); c.setId(10L);
        Mockito.when(companyService.getCompanyById(10L)).thenReturn(Optional.of(c));

        ServiceCategory saved = sample(100L);
        saved.setName("Nails");
        Mockito.when(serviceCategoryService.createServiceCategory(any(ServiceCategory.class))).thenReturn(saved);

        mvc.perform(post("/api/service-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Nails")));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateServiceCategoryRequest req = new UpdateServiceCategoryRequest();
        req.setName("Makeup");
        req.setCompanyId(10L);

        Company c = new Company(); c.setId(10L);
        Mockito.when(companyService.getCompanyById(10L)).thenReturn(Optional.of(c));

        ServiceCategory updated = sample(200L);
        updated.setName("Makeup");
        Mockito.when(serviceCategoryService.updateServiceCategory(eq(200L), any(ServiceCategory.class))).thenReturn(updated);

        mvc.perform(put("/api/service-categories/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Makeup")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/service-categories/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(serviceCategoryService).deleteServiceCategory(300L);
    }
}
