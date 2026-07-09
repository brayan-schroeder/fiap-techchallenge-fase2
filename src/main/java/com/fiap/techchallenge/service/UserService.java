package com.fiap.techchallenge.service;

import com.fiap.techchallenge.dto.LoginRequest;
import com.fiap.techchallenge.entity.User;
import com.fiap.techchallenge.exception.DuplicateEmailException;
import com.fiap.techchallenge.exception.DuplicateLoginException;
import com.fiap.techchallenge.exception.InvalidCredentialsException;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
import com.fiap.techchallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User create(User user) {

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException(
                    "Email already registered"
            );
        }

        if (repository.findByLogin(user.getLogin()).isPresent()) {
            throw new DuplicateLoginException(
                    "Login already registered"
            );
        }

        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> search(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"
                        )
                );
    }

    public User update(Long id, User data) {

        User user = findById(id);

        if (!user.getEmail().equals(data.getEmail())
                && repository.findByEmail(data.getEmail()).isPresent()) {

            throw new DuplicateEmailException(
                    "Email already registered"
            );
        }

        if (!user.getLogin().equals(data.getLogin())
                && repository.findByLogin(data.getLogin()).isPresent()) {

            throw new DuplicateLoginException(
                    "Login already registered"
            );
        }

        user.setName(data.getName());
        user.setEmail(data.getEmail());
        user.setLogin(data.getLogin());
        user.setAddress(data.getAddress());
        user.setRole(data.getRole());

        return repository.save(user);
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
}