package org.soa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompanyAndReservationServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyAndReservationServicesApplication.class, args);
    }

}
