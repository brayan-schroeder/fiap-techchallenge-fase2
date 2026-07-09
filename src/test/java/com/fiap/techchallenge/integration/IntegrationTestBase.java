package com.fiap.techchallenge.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class IntegrationTestBase {


    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(
                    "postgres:16"
            );


    static {
        postgres.start();
    }


    @DynamicPropertySource
    static void configure(
            DynamicPropertyRegistry registry
    ) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }


    @Autowired
    protected TestRestTemplate restTemplate;
}