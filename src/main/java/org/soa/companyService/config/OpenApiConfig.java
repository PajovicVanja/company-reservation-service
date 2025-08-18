package org.soa.companyService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Company & Reservation Service API",
                version = "v1",
                description = "OpenAPI documentation for Company and related resources (companies, locations, service categories, services, business hours).",
                contact = @Contact(name = "API Support", email = "support@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Local"),
                @Server(url = "https://api.example.com", description = "Prod")
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI companyReservationOpenAPI() {
        return new OpenAPI();
    }

    @Bean
    public GroupedOpenApi companiesGroup() {
        return GroupedOpenApi.builder()
                .group("companies")
                .pathsToMatch("/api/companies/**")
                .build();
    }

    @Bean
    public GroupedOpenApi locationsGroup() {
        return GroupedOpenApi.builder()
                .group("locations")
                .pathsToMatch("/api/locations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi serviceCategoriesGroup() {
        return GroupedOpenApi.builder()
                .group("service-categories")
                .pathsToMatch("/api/service-categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi servicesGroup() {
        return GroupedOpenApi.builder()
                .group("services")
                .pathsToMatch("/api/services/**")
                .build();
    }

    @Bean
    public GroupedOpenApi businessHoursGroup() {
        return GroupedOpenApi.builder()
                .group("business-hours")
                .pathsToMatch("/api/business-hours/**")
                .build();
    }

    @Bean
    public GroupedOpenApi notificationsGroup() {
        return GroupedOpenApi.builder()
                .group("notifications")
                .pathsToMatch("/api/notifications/**", "/api/sms-notification-configs/**")
                .build();
    }
}
