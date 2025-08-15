package org.soa.companyService.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SmsFaasClient {

    private final RestTemplate restTemplate;

    @Value("${SMS_FAAS_GRAPHQL_URL}")
    private String graphqlUrl;

    @Value("${SMS_SENDER_ID:}")
    private String senderId;

    public SmsFaasClient(@Qualifier("smsRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> sendSms(String phone,
                                       String templateId,
                                       String message,
                                       Map<String, String> variables) {
        String query = "mutation($in:SendSmsInput!){ sendSms(input:$in){ id status phone message createdAt } }";

        Map<String, Object> input = new HashMap<>();
        input.put("phone", phone);
        if (templateId != null && !templateId.isBlank()) input.put("templateId", templateId);
        if (message != null && !message.isBlank()) input.put("message", message);

        // map variables: Map<String,String> -> List<{key,value}>
        List<Map<String, String>> varList = new ArrayList<>();
        if (variables != null) {
            for (Map.Entry<String, String> e : variables.entrySet()) {
                Map<String, String> kv = new HashMap<>();
                kv.put("key", e.getKey());
                kv.put("value", e.getValue() == null ? "" : e.getValue());
                varList.add(kv);
            }
        }
        if (!varList.isEmpty()) input.put("variables", varList);

        if (senderId != null && !senderId.isBlank()) input.put("senderId", senderId);

        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", Map.of("in", input));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                    graphqlUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            Map<String, Object> response = resp.getBody();
            if (response == null) throw new RuntimeException("Empty response from sms-faas");
            if (response.containsKey("errors")) {
                throw new RuntimeException("sms-faas error: " + response.get("errors").toString());
            }

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data == null || data.get("sendSms") == null) {
                throw new RuntimeException("sms-faas: sendSms missing in response");
            }
            // sendSms: { id, status, phone, message, createdAt }
            return (Map<String, Object>) data.get("sendSms");

        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("sms-faas HTTP " + e.getStatusCode() + ": " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("sms-faas call failed: " + e.getMessage(), e);
        }
    }
}
