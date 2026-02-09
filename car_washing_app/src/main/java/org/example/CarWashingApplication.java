package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Car Washing Service Management API",
        version = "1.0.0",
        description = "REST API for managing car washing services, cleaners, customers, and subscriptions"
    )
)
public class CarWashingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarWashingApplication.class, args);
    }
}
