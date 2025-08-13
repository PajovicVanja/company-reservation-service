package org.soa.companyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soa.companyService.controller.LocationController;
import org.soa.companyService.dto.CreateLocationRequest;
import org.soa.companyService.dto.UpdateLocationRequest;
import org.soa.companyService.model.Location;
import org.soa.companyService.service.LocationService;
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

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockitoBean private LocationService locationService;

    private Location sample(Long id) {
        Location l = new Location();
        l.setId(id);
        l.setStreet("Main");
        l.setNumber("1A");
        return l;
    }

    @Test
    void getAll_ok() throws Exception {
        Mockito.when(locationService.getAllLocations()).thenReturn(List.of(sample(1L)));
        mvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].street", is("Main")));
    }

    @Test
    void getById_found() throws Exception {
        Mockito.when(locationService.getLocationById(9L)).thenReturn(Optional.of(sample(9L)));
        mvc.perform(get("/api/locations/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)));
    }

    @Test
    void post_create_ok() throws Exception {
        CreateLocationRequest req = new CreateLocationRequest();
        req.setName("Elm");
        req.setNumber("22");
        req.setParentLocationId(null);

        Location saved = sample(100L);
        saved.setStreet("Elm");
        saved.setNumber("22");

        Mockito.when(locationService.createLocation(any(Location.class))).thenReturn(saved);

        mvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.street", is("Elm")));
    }

    @Test
    void put_update_ok() throws Exception {
        UpdateLocationRequest req = new UpdateLocationRequest();
        req.setName("Oak");
        req.setNumber("7B");

        Location updated = sample(200L);
        updated.setStreet("Oak");
        updated.setNumber("7B");

        Mockito.when(locationService.updateLocation(eq(200L), any(Location.class))).thenReturn(updated);

        mvc.perform(put("/api/locations/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", is("Oak")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/locations/300"))
                .andExpect(status().isNoContent());
        Mockito.verify(locationService).deleteLocation(300L);
    }
}
