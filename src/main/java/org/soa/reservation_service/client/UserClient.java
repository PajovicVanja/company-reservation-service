package org.soa.reservation_service.client;

import org.soa.reservation_service.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;
import java.util.Optional;

@Component
public class UserClient {

    private static final Logger log = Logger.getLogger(UserClient.class.getName());

    private final RestTemplate restTemplate;
    private final String baseUrl;

    /**
     * Base URL defaults to docker network address of user-service.
     * Env var mapping: USER_SERVICE_URL -> user.service.url
     * e.g. http://user-service:3000/api  or  http://localhost:3000/api
     */
    public UserClient(RestTemplate restTemplate,
                      @Value("${user.service.url:http://user-service:3000/api}") String baseUrl) {
        this.restTemplate = restTemplate;
        // normalize: no trailing slash
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
        return getUserById(id).map(UserDto::getName).orElse(null);
    }

    private static HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        return h;
    }
}
