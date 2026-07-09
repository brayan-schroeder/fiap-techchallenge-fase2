package com.fiap.techchallenge.application.service;

import com.fiap.techchallenge.application.dto.CreateUserRequest;
import com.fiap.techchallenge.application.dto.LoginRequest;
import com.fiap.techchallenge.application.dto.UpdateUserRequest;
import com.fiap.techchallenge.application.dto.UserResponse;
import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.domain.exception.DuplicateEmailException;
import com.fiap.techchallenge.domain.exception.DuplicateLoginException;
import com.fiap.techchallenge.domain.exception.InvalidCredentialsException;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserTypeService userTypeService;

    public UserResponse create(CreateUserRequest request) {

        if (repository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateEmailException(
                    "Email already registered"
            );
        }

        if (repository.findByLogin(request.login()).isPresent()) {
            throw new DuplicateLoginException(
                    "Login already registered"
            );
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setPassword(request.password());
        user.setAddress(request.address());

        user.setUserType(
                userTypeService.findById(
                        request.userTypeId()
                )
        );

        return toResponse(repository.save(user));
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UserResponse> search(String name) {
        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"
                        )
                );
    }

    public UserResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {

        User user = findById(id);

        if (!user.getEmail().equals(request.email())
                && repository.findByEmail(request.email()).isPresent()) {

            throw new DuplicateEmailException(
                    "Email already registered"
            );
        }

        if (!user.getLogin().equals(request.login())
                && repository.findByLogin(request.login()).isPresent()) {

            throw new DuplicateLoginException(
                    "Login already registered"
            );
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setAddress(request.address());

        user.setUserType(
                userTypeService.findById(
                        request.userTypeId()
                )
        );

        return toResponse(repository.save(user));
    }

    public void updatePassword(Long id, String password) {

        User user = findById(id);

        user.setPassword(password);

        repository.save(user);
    }

    public void delete(Long id) {

        User user = findById(id);

        repository.delete(user);
    }

    public boolean login(LoginRequest request) {

        boolean authenticated = repository
                .findByLoginAndPassword(
                        request.login(),
                        request.password()
                )
                .isPresent();

        if (!authenticated) {
            throw new InvalidCredentialsException(
                    "Invalid login or password"
            );
        }

        return true;
    }

    private UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getAddress(),
                user.getUserType().getName(),
                user.getLastUpdate()
        );
    }
}