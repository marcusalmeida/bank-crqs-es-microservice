package com.bank.es.exceptions;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException() {}

    public InvalidEventException(String message) {
        super("invalid event: " + message);
    }
}