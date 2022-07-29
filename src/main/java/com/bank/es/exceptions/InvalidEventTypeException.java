package com.bank.es.exceptions;

public class InvalidEventTypeException extends RuntimeException {
    public InvalidEventTypeException() {}

    public InvalidEventTypeException(String eventType) {
        super("invalid event type: " + eventType);
    }
}
