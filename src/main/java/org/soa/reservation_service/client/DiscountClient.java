package org.soa.reservation_service.client;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class DiscountClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Logger logger = Logger.getLogger(DiscountClient.class.getName());

    public DiscountClient(@Value("${DISCOUNT_SERVICE_URL}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    public void addLoyaltyPoints(Integer userId, Integer companyId, Long serviceId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serviceId", serviceId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers());
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/api/user-points/user/" + userId + "/company/"+ companyId,
                entity,
                Map.class
        );
        logger.info("Tax added successfully: " + response.getBody());
    }
}
