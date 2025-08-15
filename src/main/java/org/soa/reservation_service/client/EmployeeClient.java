package org.soa.reservation_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.*;
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

    /** Contract: 404 when missing OR soft-deleted. */
    public void assertEmployeeActive(Long employeeId) {
        try {
            var resp = restTemplate.exchange(
                    baseUrl + "/employees/" + employeeId,
                    HttpMethod.GET, new HttpEntity<>(headers()), Map.class);
            var body = resp.getBody();
            if (body == null) throw new IllegalStateException("Employee payload empty");
            Object active = body.get("active");
            if (active instanceof Boolean && !((Boolean) active)) {
                throw new IllegalStateException("Employee is inactive");
            }
        } catch (HttpClientErrorException.NotFound nf) {
            throw new RuntimeException("Employee not found/inactive: " + employeeId, nf);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to validate employee " + employeeId, ex);
        }
    }

    /** Contract: GET /employees/{id}/skills/ -> [{"service_id": 7}, ...] */
    public void assertEmployeeHasSkill(Long employeeId, Long serviceId) {
        try {
            var resp = restTemplate.exchange(
                    baseUrl + "/employees/" + employeeId + "/skills/",
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            var skills = resp.getBody();
            boolean ok = false;
            if (skills != null) {
                for (Object o : skills) {
                    if (o instanceof Map<?, ?> m) {
                        Object sid = m.get("service_id");
                        if (sid != null && Long.parseLong(String.valueOf(sid)) == serviceId) { ok = true; break; }
                    } else {
                        // tolerate older shapes like [1,3,5]
                        long sid = Long.parseLong(String.valueOf(o));
                        if (sid == serviceId) { ok = true; break; }
                    }
                }
            }
            if (!ok) throw new IllegalStateException("Employee lacks required skill for service " + serviceId);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to verify employee skills", ex);
        }
    }

    /** Contract: GET /employees/{id}/availability/ -> slots with day_of_week, time_from, time_to (HH:MM:SS) */
    public void assertEmployeeAvailable(Long employeeId, LocalDateTime start, short durationMinutes) {
        LocalDateTime end = start.plusMinutes(durationMinutes);
        int targetDow = start.getDayOfWeek().getValue(); // ISO: Mon=1..Sun=7 (matches contract recommendation)

        var resp = restTemplate.exchange(
                baseUrl + "/employees/" + employeeId + "/availability/",
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        var slots = resp.getBody();
        if (slots == null || slots.isEmpty()) {
            throw new IllegalStateException("Employee has no availability configured");
        }

        LocalTime startT = start.toLocalTime();
        LocalTime endT   = end.toLocalTime();

        boolean fits = false;
        for (Map<String, Object> slot : slots) {
            int dow = toInt(slot.get("day_of_week"));
            if (dow != targetDow) continue;

            LocalTime from = LocalTime.parse(String.valueOf(slot.get("time_from")));
            LocalTime to   = LocalTime.parse(String.valueOf(slot.get("time_to")));

            // Allow inclusive start, exclusive end to chain slots cleanly
            if (!startT.isBefore(from) && !endT.isAfter(to)) {
                fits = true;
                break;
            }
        }
        if (!fits) {
            throw new IllegalStateException("Employee not available for " + durationMinutes + " minutes at " + start);
        }
    }

    /** Optional helper if you later intersect free-slots with employee availability. */
    public List<Map<String, Object>> getAvailabilityWindows(Long employeeId, LocalDate day, Long locationId) {
        var resp = restTemplate.exchange(
                baseUrl + "/employees/" + employeeId + "/availability/",
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        var slots = resp.getBody();
        if (slots == null) return List.of();
        int dow = day.getDayOfWeek().getValue();
        return slots.stream()
                .filter(s -> toInt(s.get("day_of_week")) == dow)
                .filter(s -> locationId == null || (s.get("location_id") == null || Long.parseLong(String.valueOf(s.get("location_id"))) == locationId))
                .toList();
    }

    private static int toInt(Object o) { return Integer.parseInt(String.valueOf(o)); }

    public Map<String, Object> getEmployeeRaw(Long employeeId) {
        var resp = restTemplate.exchange(
                baseUrl + "/employees/" + employeeId,
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                new ParameterizedTypeReference<Map<String, Object>>() {});
        return resp.getBody();
    }

    public List<Map<String, Object>> getSkillsRaw(Long employeeId) {
        var resp = restTemplate.exchange(
                baseUrl + "/employees/" + employeeId + "/skills/",
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        return resp.getBody();
    }

    public List<Map<String, Object>> getAvailabilityRaw(Long employeeId) {
        var resp = restTemplate.exchange(
                baseUrl + "/employees/" + employeeId + "/availability/",
                HttpMethod.GET,
                new HttpEntity<>(headers()),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        return resp.getBody();
    }
}
