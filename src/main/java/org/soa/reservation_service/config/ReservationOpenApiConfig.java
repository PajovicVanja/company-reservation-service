package org.soa.reservation_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration("reservationOpenApiConfig") // unique bean name to avoid clash with companyService OpenApiConfig
public class ReservationOpenApiConfig {

    @Bean
    @Primary // make this the preferred OpenAPI bean if multiple exist
    public OpenAPI reservationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation Service API")
                        .version("v1")
                        .description("OpenAPI documentation for Reservations, Payments, Payment Types, and Statuses."));
    }
}
