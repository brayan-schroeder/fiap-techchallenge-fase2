package com.fiap.techchallenge.unit;

import com.fiap.techchallenge.application.dto.CreateUserRequest;
import com.fiap.techchallenge.application.dto.LoginRequest;
import com.fiap.techchallenge.application.dto.UpdateUserRequest;
import com.fiap.techchallenge.application.service.UserService;
import com.fiap.techchallenge.application.service.UserTypeService;
import com.fiap.techchallenge.domain.exception.DuplicateEmailException;
import com.fiap.techchallenge.domain.exception.DuplicateLoginException;
import com.fiap.techchallenge.domain.exception.InvalidCredentialsException;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.domain.model.UserType;
import com.fiap.techchallenge.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserTypeService userTypeService;


    @InjectMocks
    private UserService service;



    @Test
    void shouldCreateUser() {

        CreateUserRequest request =
                new CreateUserRequest(
                        "Brayan",
                        "email@test.com",
                        "brayan",
                        "123",
                        "Address",
                        1L
                );


        UserType type = new UserType();
        type.setId(1L);
        type.setName("OWNER");


        when(userTypeService.findById(1L))
                .thenReturn(type);


        when(repository.save(any(User.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );


        var result =
                service.create(request);


        assertEquals(
                "Brayan",
                result.name()
        );


        verify(repository)
                .save(any());
    }



    @Test
    void shouldThrowWhenEmailExists() {

        CreateUserRequest request =
                new CreateUserRequest(
                        "Brayan",
                        "email@test.com",
                        "login",
                        "123",
                        null,
                        1L
                );


        when(repository.findByEmail(
                request.email()
        ))
                .thenReturn(
                        Optional.of(new User())
                );


        assertThrows(
                DuplicateEmailException.class,
                () -> service.create(request)
        );
    }



    @Test
    void shouldThrowWhenLoginExists() {

        CreateUserRequest request =
                new CreateUserRequest(
                        "Brayan",
                        "email@test.com",
                        "login",
                        "123",
                        null,
                        1L
                );


        when(repository.findByLogin(
                request.login()
        ))
                .thenReturn(
                        Optional.of(new User())
                );


        assertThrows(
                DuplicateLoginException.class,
                () -> service.create(request)
        );
    }



    @Test
    void shouldFindUserById() {

        User user = new User();

        user.setId(1L);


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        assertEquals(
                1L,
                service.findById(1L).getId()
        );
    }



    @Test
    void shouldThrowWhenUserNotFound() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L)
        );
    }



    @Test
    void shouldListUsers() {

        UserType type = new UserType();

        type.setId(1L);
        type.setName("OWNER");


        User user = new User();

        user.setId(1L);
        user.setName("Brayan");
        user.setEmail("email@test.com");
        user.setLogin("brayan");
        user.setUserType(type);


        when(repository.findAll())
                .thenReturn(
                        List.of(user)
                );


        var result =
                service.findAll();


        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "OWNER",
                result.get(0).userType()
        );
    }

    @Test
    void shouldUpdateUser() {

        UserType type = new UserType();
        type.setId(1L);
        type.setName("CUSTOMER");


        User user = new User();

        user.setId(1L);
        user.setName("Old");
        user.setEmail("old@test.com");
        user.setLogin("old");


        UpdateUserRequest request =
                new UpdateUserRequest(
                        "Brayan",
                        "new@test.com",
                        "newlogin",
                        "New Address",
                        1L
                );


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(repository.findByEmail(
                request.email()
        ))
                .thenReturn(
                        Optional.empty()
                );


        when(repository.findByLogin(
                request.login()
        ))
                .thenReturn(
                        Optional.empty()
                );


        when(userTypeService.findById(1L))
                .thenReturn(type);


        when(repository.save(any(User.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );


        var response =
                service.update(
                        1L,
                        request
                );


        assertEquals(
                "Brayan",
                response.name()
        );

        assertEquals(
                "new@test.com",
                response.email()
        );

        assertEquals(
                "CUSTOMER",
                response.userType()
        );


        verify(repository)
                .save(user);
    }

    @Test
    void shouldThrowWhenUpdatingWithExistingLogin() {

        User user = new User();

        user.setId(1L);
        user.setEmail("email@test.com");
        user.setLogin("old");


        UpdateUserRequest request =
                new UpdateUserRequest(
                        "Brayan",
                        "email@test.com",
                        "newlogin",
                        null,
                        1L
                );


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(repository.findByLogin(
                "newlogin"
        ))
                .thenReturn(
                        Optional.of(new User())
                );


        assertThrows(
                DuplicateLoginException.class,
                () ->
                        service.update(
                                1L,
                                request
                        )
        );
    }

    @Test
    void shouldThrowWhenUpdatingWithExistingEmail() {

        User user = new User();

        user.setId(1L);
        user.setEmail("old@test.com");
        user.setLogin("login");


        UpdateUserRequest request =
                new UpdateUserRequest(
                        "Brayan",
                        "new@test.com",
                        "login",
                        null,
                        1L
                );


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(repository.findByEmail(
                "new@test.com"
        ))
                .thenReturn(
                        Optional.of(new User())
                );


        assertThrows(
                DuplicateEmailException.class,
                () ->
                        service.update(
                                1L,
                                request
                        )
        );
    }



    @Test
    void shouldUpdatePassword() {

        User user = new User();

        user.setId(1L);


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        service.updatePassword(
                1L,
                "123456"
        );


        assertEquals(
                "123456",
                user.getPassword()
        );


        verify(repository)
                .save(user);
    }



    @Test
    void shouldDeleteUser() {

        User user = new User();

        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        service.delete(1L);


        verify(repository)
                .delete(user);
    }



    @Test
    void shouldLoginSuccessfully() {

        when(
                repository.findByLoginAndPassword(
                        "brayan",
                        "123"
                )
        )
                .thenReturn(
                        Optional.of(new User())
                );


        assertTrue(
                service.login(
                        new LoginRequest(
                                "brayan",
                                "123"
                        )
                )
        );
    }



    @Test
    void shouldThrowInvalidCredentials() {

        when(
                repository.findByLoginAndPassword(
                        any(),
                        any()
                )
        )
                .thenReturn(Optional.empty());


        assertThrows(
                InvalidCredentialsException.class,
                () ->
                        service.login(
                                new LoginRequest(
                                        "x",
                                        "x"
                                )
                        )
        );
    }
}