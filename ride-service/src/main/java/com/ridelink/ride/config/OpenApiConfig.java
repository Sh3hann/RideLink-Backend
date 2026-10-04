package com.ridelink.ride.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8083}")
    private String serverPort;

    @Bean
    public OpenAPI rideServiceOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("RideLink — Ride Management & Lifecycle Service API")
                        .description("Microservice 3 (Member 3): Manages ride requests, automated & manual driver assignment, "
                                + "ride lifecycle state machine (REQUESTED -> ASSIGNED -> ACCEPTED -> IN_PROGRESS -> COMPLETED / CANCELLED), "
                                + "and interservice orchestration via Spring Cloud OpenFeign.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IT3130 - AD Group Project (Member 3)")
                                .email("member3@ridelink.internal"))
                        .license(new License().name("Academic Use Only — SLIIT IT3130")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide JWT token from Account Service (Member 1)")));
    }
}
