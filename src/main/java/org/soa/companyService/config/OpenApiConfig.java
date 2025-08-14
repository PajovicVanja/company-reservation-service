package org.soa.companyService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI companyReservationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Company & Reservation Service API")
                        .version("v1")
                        .description("OpenAPI documentation for Company and related resources (companies, locations, service categories, services, business hours)."));
    }
}
