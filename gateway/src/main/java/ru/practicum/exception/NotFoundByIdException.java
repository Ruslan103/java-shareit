package ru.practicum.exception;

public class NotFoundByIdException extends RuntimeException {
    public NotFoundByIdException(String message) {
        super(message);
    }
}
