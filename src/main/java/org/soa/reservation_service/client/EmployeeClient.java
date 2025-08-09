// src/main/java/org/soa/reservation_service/client/EmployeeClient.java
package org.soa.reservation_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String bearer;

    public EmployeeClient(RestTemplate restTemplate,
                          @Value("${employee.service.url}") String baseUrl,
                          @Value("${employee.service.token:}") String token) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.bearer = (token == null || token.isBlank()) ? null : "Bearer " + token;
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (bearer != null) h.set(HttpHeaders.AUTHORIZATION, bearer);
        return h;
    }

    public void assertEmployeeActive(Long employeeId) {
        try {
            var resp = restTemplate.exchange(
                    baseUrl + "/employees/" + employeeId,
                    HttpMethod.GET, new HttpEntity<>(headers()), Map.class);
            var body = resp.getBody();
            if (body == null || Boolean.FALSE.equals(body.get("active"))) {
                throw new IllegalStateException("Employee is inactive");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Employee not found/active: " + employeeId, ex);
        }
    }

    public void assertEmployeeHasSkill(Long employeeId, Long serviceId) {
        try {
            var resp = restTemplate.exchange(
                    baseUrl + "/employees/" + employeeId + "/skills",
                    HttpMethod.GET, new HttpEntity<>(headers()), List.class);
            List<?> skills = resp.getBody();
            if (skills == null || skills.stream().noneMatch(s -> String.valueOf(s).equals(String.valueOf(serviceId)))) {
                throw new IllegalStateException("Employee does not have required skill for service " + serviceId);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to verify employee skills", ex);
        }
    }

    public void assertEmployeeAvailable(Long employeeId, LocalDateTime start, short durationMinutes) {
        // TODO implement availability check against employee service:
        // 1) GET /employees/{id}/availability
        // 2) ensure [start, start+duration] fits within a slot of that day
    }

    public List<Map<String, Object>> getAvailabilityWindows(Long employeeId, LocalDate day, Long locationId) {
        // TODO call employee availability endpoint and filter by day/location
        return List.of(); // stub for now; intersection can be added later
    }
}
