package com.fiap.techchallenge.unit;

import com.fiap.techchallenge.application.dto.CreateUserTypeRequest;
import com.fiap.techchallenge.application.dto.UpdateUserTypeRequest;
import com.fiap.techchallenge.application.service.UserTypeService;
import com.fiap.techchallenge.domain.exception.BusinessException;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.UserType;
import com.fiap.techchallenge.infrastructure.repository.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {


    @Mock
    private UserTypeRepository repository;


    @InjectMocks
    private UserTypeService service;



    @Test
    void shouldCreateUserType() {

        CreateUserTypeRequest request =
                new CreateUserTypeRequest(
                        "ADMIN"
                );


        when(repository.save(any(UserType.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );


        UserType result =
                service.create(request);


        assertEquals(
                "ADMIN",
                result.getName()
        );


        verify(repository)
                .save(any());
    }



    @Test
    void shouldFindAllUserTypes() {

        UserType type =
                new UserType();

        type.setName("OWNER");


        when(repository.findAll())
                .thenReturn(
                        List.of(type)
                );


        List<UserType> result =
                service.findAll();


        assertEquals(
                1,
                result.size()
        );
    }



    @Test
    void shouldFindUserTypeById() {

        UserType type =
                new UserType();

        type.setId(1L);
        type.setName("OWNER");


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(type)
                );


        UserType result =
                service.findById(1L);


        assertEquals(
                "OWNER",
                result.getName()
        );
    }



    @Test
    void shouldThrowWhenUserTypeNotFound() {

        when(repository.findById(99L))
                .thenReturn(
                        Optional.empty()
                );


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L)
        );
    }



    @Test
    void shouldUpdateUserType() {

        UserType type =
                new UserType();

        type.setId(1L);
        type.setName("OLD");


        UpdateUserTypeRequest request =
                new UpdateUserTypeRequest(
                        "NEW"
                );


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(type)
                );


        when(repository.save(type))
                .thenReturn(type);


        UserType result =
                service.update(
                        1L,
                        request
                );


        assertEquals(
                "NEW",
                result.getName()
        );


        verify(repository)
                .save(type);
    }



    @Test
    void shouldDeleteUserType() {

        UserType type =
                new UserType();

        type.setId(3L);
        type.setName("ADMIN");


        when(repository.findById(3L))
                .thenReturn(
                        Optional.of(type)
                );


        service.delete(3L);


        verify(repository)
                .delete(type);
    }



    @Test
    void shouldNotDeleteDefaultOwnerType() {

        UserType type =
                new UserType();

        type.setId(1L);
        type.setName("OWNER");


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(type)
                );


        assertThrows(
                BusinessException.class,
                () -> service.delete(1L)
        );


        verify(repository, never())
                .delete(any());
    }



    @Test
    void shouldNotDeleteDefaultCustomerType() {

        UserType type =
                new UserType();

        type.setId(2L);
        type.setName("CUSTOMER");


        when(repository.findById(2L))
                .thenReturn(
                        Optional.of(type)
                );


        assertThrows(
                BusinessException.class,
                () -> service.delete(2L)
        );


        verify(repository, never())
                .delete(any());
    }
}