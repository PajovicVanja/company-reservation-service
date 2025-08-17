package org.soa.reservation_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration("reservationOpenApiConfig")
@OpenAPIDefinition(
        info = @Info(
                title = "Reservation Service API",
                version = "v1",
                description = "OpenAPI documentation for Reservations, Payments, Payment Types, and Statuses.",
                contact = @Contact(name = "API Support", email = "support@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local"),
                @Server(url = "https://reservation.example.com", description = "Production")
        }
)
public class ReservationOpenApiConfig {

    @Bean
    @Primary
    public OpenAPI reservationOpenAPI() {
        return new OpenAPI();
    }

    @Bean
    public GroupedOpenApi reservationsGroup() {
        return GroupedOpenApi.builder()
                .group("reservations")
                .pathsToMatch("/api/reservations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentsGroup() {
        return GroupedOpenApi.builder()
                .group("payments")
                .pathsToMatch("/api/payments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentTypesGroup() {
        return GroupedOpenApi.builder()
                .group("payment-types")
                .pathsToMatch("/api/payment-types/**")
                .build();
    }

    @Bean
    public GroupedOpenApi statusesGroup() {
        return GroupedOpenApi.builder()
                .group("statuses")
                .pathsToMatch("/api/statuses/**")
                .build();
    }

    @Bean
    public GroupedOpenApi probeGroup() {
        return GroupedOpenApi.builder()
                .group("_probe")
                .pathsToMatch("/api/_probe/**")
                .build();
    }
}
