package org.soa.reservation_service.client;

import org.soa.reservation_service.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class UserClient {
    private static final Logger log = Logger.getLogger(UserClient.class.getName());
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserClient(RestTemplate restTemplate,
                      @Value("${user.service.url:http://user-service:3000/api}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public Optional<UserDto> getUserById(Integer id) {
        if (id == null || id <= 0) return Optional.empty();
        try {
            ResponseEntity<UserDto> resp = restTemplate.exchange(
                    baseUrl + "/users/" + id,
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    new ParameterizedTypeReference<UserDto>() {}
            );
            return Optional.ofNullable(resp.getBody());
        } catch (RestClientException ex) {
            log.warning("[UserClient] Failed to fetch user " + id + ": " + ex.getMessage());
            return Optional.empty();
        }
    }

    public String safeGetName(Integer id) {
        return getUserById(id).map(UserDto::getFullName).orElse(null);
    }

    public Long safeGetPhoneAsLong(Integer id) {
        return getUserById(id).map(UserDto::getPhoneNumber)
                .map(UserClient::normalizePhoneToLong)
                .orElse(null);
    }

    private static HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    private static Long normalizePhoneToLong(String raw) {
        if (raw == null) return null;
        String digits = raw.replaceAll("\\D", "");
        if (digits.isEmpty()) return null;
        try { return Long.parseLong(digits); } catch (NumberFormatException e) { return null; }
    }
}
