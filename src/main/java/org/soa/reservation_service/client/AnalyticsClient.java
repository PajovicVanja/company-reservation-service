package org.soa.reservation_service.client;

import org.soa.reservation_service.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Thin client for the Analytics Service.
 * - POST /time/ to ensure a calendar date exists in the time dimension
 * - GET /analytics/reservations-per-month to refresh (or warm) aggregates
 *
 * NOTE: All calls are best-effort; failures are logged and swallowed.
 */
@Component
@ConditionalOnProperty(name = "analytics.service.url") // bean exists ONLY if property is set
public class AnalyticsClient {

    private static final Logger log = Logger.getLogger(AnalyticsClient.class.getName());

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AnalyticsClient(RestTemplate restTemplate,
                           @Value("${analytics.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        // normalize: no trailing slash
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private static HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    /** Create/ensure a time dimension row exists for the reservation date. */
    public void ensureTimeDim(Timestamp ts) {
        if (ts == null) return;
        LocalDate d = ts.toLocalDateTime().toLocalDate();

        Map<String, Object> body = new HashMap<>();
        body.put("date", d.toString());      // yyyy-MM-dd
        body.put("year", d.getYear());
        body.put("month", d.getMonthValue());
        body.put("day", d.getDayOfMonth());

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, jsonHeaders());
            restTemplate.postForEntity(baseUrl + "/time/", entity, Map.class);
            log.info("[Analytics] /time/ upsert ok for " + d);
        } catch (RestClientException ex) {
            log.warning("[Analytics] ensureTimeDim failed (skipping): " + ex.getMessage());
        }
    }

    /** Touch an aggregate endpoint that reflects new reservations (best fit for creation). */
    public void refreshReservationsPerMonth() {
        try {
            restTemplate.exchange(
                    baseUrl + "/analytics/reservations-per-month",
                    HttpMethod.GET,
                    new HttpEntity<>(jsonHeaders()),
                    String.class
            );
            log.info("[Analytics] refreshed reservations-per-month");
        } catch (RestClientException ex) {
            log.warning("[Analytics] refresh reservations-per-month failed (skipping): " + ex.getMessage());
        }
    }

    /** Convenience wrapper executed on reservation creation. */
    public void onReservationCreated(Reservation reservation) {
        ensureTimeDim(reservation != null ? reservation.getDate() : null);
        // choose the most appropriate aggregate for creation
        refreshReservationsPerMonth();
    }
}
