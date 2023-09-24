package ru.practicum.exception;

public class StatusApprovedException extends RuntimeException {
    public StatusApprovedException(String message) {
        super(message);
    }
}
