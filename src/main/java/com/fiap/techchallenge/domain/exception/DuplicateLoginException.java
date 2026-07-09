package com.fiap.techchallenge.domain.exception;

public class DuplicateLoginException extends RuntimeException {

    public DuplicateLoginException(String message) {
        super(message);
    }
}