package org.soa.companyService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // Unique bean name so it doesn't collide with other RestTemplate beans
    @Bean(name = "smsRestTemplate")
    public RestTemplate smsRestTemplate() {
        return new RestTemplate();
    }
}
