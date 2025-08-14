package org.soa.reservation_service.client;

import org.soa.companyService.dto.CreateCompanyRequest;
import org.soa.companyService.model.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AdminClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Logger logger = Logger.getLogger(AdminClient.class.getName());

    public AdminClient(@Value("${ADMIN_SERVICE_URL}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return h;
    }

    public void createCompany(Company company) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", company.getCompanyName());
        requestBody.put("accountable", company.getFirstName() + " " + company.getLastName());
        requestBody.put("email", company.getEmail());
        requestBody.put("description", company.getDescription());
        requestBody.put("phoneNumber", company.getPhoneNumber());
        requestBody.put("address", company.getAddress());
        requestBody.put("location", company.getLocation().getStreet());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers());
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/v1/companies", entity, Map.class);
        logger.info("Tax added successfully: " + response.getBody());
    }
}
