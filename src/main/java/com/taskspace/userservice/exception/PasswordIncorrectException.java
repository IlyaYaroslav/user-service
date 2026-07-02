package com.taskspace.userservice.exception;

public class PasswordIncorrectException extends RuntimeException {

    public PasswordIncorrectException(String message) {
        super("Password is incorrect " + message);

    }
}
