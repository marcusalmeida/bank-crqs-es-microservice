package com.bank.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Component;


@OpenAPIDefinition(info = @Info(title = "Spring CQRS and Event Sourcing Microservice",
    description = "Spring Postgresql MongoDB Kafka CQRS and Event Sourcing Microservice"))
@Component
public class SwaggerOpenAPIConfiguration {
}