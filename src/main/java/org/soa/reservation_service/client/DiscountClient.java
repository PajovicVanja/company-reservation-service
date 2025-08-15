// client/DiscountClient.java
package org.soa.reservation_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Component
@ConditionalOnProperty(name = "discount.service.url") // bean exists ONLY if property is set
public class DiscountClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Logger logger = Logger.getLogger(DiscountClient.class.getName());

    public DiscountClient(RestTemplate restTemplate,
                          @Value("${discount.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    public void addLoyaltyPoints(Integer userId, Integer companyId, Long serviceId) {
        try {
            Map<String, Object> body = Map.of("serviceId", serviceId);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers());
            restTemplate.postForEntity(
                    baseUrl + "/api/user-points/user/" + userId + "/company/" + companyId,
                    entity, Map.class
            );
            logger.info("[Discount] loyalty points call succeeded");
        } catch (RestClientException ex) {
            // swallow failures: log & move on so reservations still work
            logger.warning("[Discount] service call failed (skipping): " + ex.getMessage());
        }
    }
}
