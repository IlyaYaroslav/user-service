package com.taskspace.userservice.exceptions;

public class PasswordIncorrectException extends RuntimeException {

    public PasswordIncorrectException(String message) {
        super("Password is incorrect " + message);
    }
}
