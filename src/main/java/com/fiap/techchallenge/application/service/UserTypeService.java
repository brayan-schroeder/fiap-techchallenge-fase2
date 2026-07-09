package com.fiap.techchallenge.application.service;

import com.fiap.techchallenge.application.dto.CreateUserTypeRequest;
import com.fiap.techchallenge.application.dto.UpdateUserTypeRequest;
import com.fiap.techchallenge.domain.exception.BusinessException;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.UserType;
import com.fiap.techchallenge.infrastructure.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTypeService {

    private final UserTypeRepository repository;

    public UserType create(CreateUserTypeRequest request) {

        UserType userType = new UserType();

        userType.setName(request.name());

        return repository.save(userType);
    }

    public List<UserType> findAll() {
        return repository.findAll();
    }

    public UserType findById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User type not found"
                        )
                );
    }


    public UserType update(
            Long id,
            UpdateUserTypeRequest request
    ) {

        UserType userType = findById(id);

        if (isDefaultType(userType)) {
            throw new BusinessException(
                    "Default user types cannot be updated"
            );
        }

        userType.setName(request.name());

        return repository.save(userType);
    }


    public void delete(Long id) {

        UserType userType = findById(id);

        if (isDefaultType(userType)) {
            throw new BusinessException(
                    "Default user types cannot be deleted"
            );
        }

        repository.delete(userType);
    }

    public boolean isOwner(UserType userType) {

        return userType.getName().equals("OWNER");
    }

    public boolean isCostumer(UserType userType) {

        return userType.getName().equals("CUSTOMER");
    }

    private boolean isDefaultType(UserType userType) {

        return isOwner(userType) || isCostumer(userType);
    }
}