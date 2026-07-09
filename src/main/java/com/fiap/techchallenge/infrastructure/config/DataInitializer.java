package com.fiap.techchallenge.infrastructure.config;

import com.fiap.techchallenge.domain.model.UserType;
import com.fiap.techchallenge.infrastructure.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;

    @Override
    public void run(String... args) {

        createUserTypeIfNotExists("OWNER");

        createUserTypeIfNotExists("CUSTOMER");
    }

    private void createUserTypeIfNotExists(String name) {

        if (userTypeRepository.findByName(name).isEmpty()) {

            UserType userType = new UserType();

            userType.setName(name);

            userTypeRepository.save(userType);
        }
    }
}