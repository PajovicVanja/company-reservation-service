package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.ServiceController;
import org.soa.companyService.dto.CreateServiceRequest;
import org.soa.companyService.dto.UpdateServiceRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
import org.soa.companyService.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private ServiceService serviceService;
    @MockitoBean private ServiceCategoryService serviceCategoryService;
    @MockitoBean private CompanyService companyService;

    private ServiceM sample(Long id) {
        ServiceM s = new ServiceM();
        s.setId(id);
        s.setName("Haircut");
        Company c = new Company(); c.setId(10L); s.setCompany(c);
        ServiceCategory cat = new ServiceCategory(); cat.setId(20L); s.setCategory(cat);
        return s;
    }

    @Test
    void getAll_byCompany_ok() throws Exception {
        Mockito.when(serviceService.getAllServices(10L)).thenReturn(List.of(sample(1L)));
        mvc.perform(get("/api/services/company/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Haircut")));
    }

    @Test
    void getAll_byCompanyAndCategory_ok() throws Exception {
        Mockito.when(serviceService.getAllServices(10L, 20L)).thenReturn(List.of(sample(2L)));
        mvc.perform(get("/api/services/company/10/category/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(2)));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(serviceService.getServiceById(5L)).thenReturn(Optional.of(sample(5L)));
        mvc.perform(get("/api/services/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }

    // ---- POST /api/services (JSON only variant) ----
    // Enable this if you change controller param to:
    // @PostMapping
    // public ResponseEntity<ServiceM> createService(@RequestBody CreateServiceRequest request,
    //     @RequestParam(name="file", required=false) MultipartFile file)
    @Test
    void post_create_jsonOnly_ok() throws Exception {
        CreateServiceRequest req = new CreateServiceRequest();
        req.setName("Massage");
        req.setDescription("Relax");
        req.setDuration((short)60);
        req.setPrice(50.0f);
        req.setCategoryId(20L);
        req.setCompanyId(10L);

        ServiceCategory cat = new ServiceCategory(); cat.setId(20L);
        Company comp = new Company(); comp.setId(10L);
        Mockito.when(serviceCategoryService.getServiceCategoryById(20L)).thenReturn(Optional.of(cat));
        Mockito.when(companyService.getCompanyById(10L)).thenReturn(Optional.of(comp));

        ServiceM saved = sample(100L);
        saved.setName("Massage");
        Mockito.when(serviceService.createService(any(ServiceM.class))).thenReturn(saved);

        mvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Massage")));
    }

    // ---- POST /api/services (multipart variant) ----
    // If you change controller signature to:
    //   createService(@RequestPart("request") CreateServiceRequest request,
    //                 @RequestPart(value="file", required=false) MultipartFile file)
    // you can enable this test:
    @Disabled("Enable after switching to @RequestPart on controller")
    @Test
    void post_create_multipart_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "s.png", "image/png", new byte[]{1,2});
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json",
                ("{" +
                        "\"name\":\"Massage\",\"description\":\"Relax\"," +
                        "\"duration\":60,\"price\":50.0," +
                        "\"categoryId\":20,\"companyId\":10}").getBytes());

        ServiceCategory cat = new ServiceCategory(); cat.setId(20L);
        Company comp = new Company(); comp.setId(10L);
        Mockito.when(serviceCategoryService.getServiceCategoryById(20L)).thenReturn(Optional.of(cat));
        Mockito.when(companyService.getCompanyById(10L)).thenReturn(Optional.of(comp));

        ServiceM saved = sample(101L);
        saved.setName("Massage");
        Mockito.when(serviceService.createService(any(ServiceM.class), any())).thenReturn(saved);

        mvc.perform(multipart("/api/services").file(file).file(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateServiceRequest req = new UpdateServiceRequest();
        req.setName("Beard");
        req.setDescription("Trim");
        req.setDuration((short)15);
        req.setPrice(10.0f);
        req.setCategoryId(20L);
        req.setCompanyId(10L);

        ServiceCategory cat = new ServiceCategory(); cat.setId(20L);
        Company comp = new Company(); comp.setId(10L);
        Mockito.when(serviceCategoryService.getServiceCategoryById(20L)).thenReturn(Optional.of(cat));
        Mockito.when(companyService.getCompanyById(10L)).thenReturn(Optional.of(comp));

        ServiceM updated = sample(200L);
        updated.setName("Beard");
        Mockito.when(serviceService.updateService(eq(200L), any(ServiceM.class))).thenReturn(updated);

        mvc.perform(put("/api/services/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Beard")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/services/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(serviceService).deleteService(300L);
    }

    @Test
    void uploadPicture_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "svc.png", "image/png", new byte[]{3,4});
        mvc.perform(multipart("/api/services/{id}/upload-picture", 77L).file(file))
                .andExpect(status().isOk());
        Mockito.verify(serviceService).uploadPicture(77L, file);
    }
}
