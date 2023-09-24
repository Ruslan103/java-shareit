package ru.practicum.exception;

public class RequestParameterException extends RuntimeException {
    public RequestParameterException(String message) {
        super(message);
    }
}
