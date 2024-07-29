package com.bteam.Booking_Beacon.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@Qualifier("authConfig")
public class AuthConfig {
    private final Environment env;

    public String getAppName() {
        return env.getProperty("spring.application.name");
    }

    public Environment getEnvironment() {
        return env;
    }
}
