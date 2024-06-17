package com.bteam.Booking_Beacon.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(
        info = @Info(
                title = "booking-service swagger docs",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
    @Bean
    @Profile("!prod")
    public OpenAPI openAPI() {
        return new OpenAPI();
    }
}