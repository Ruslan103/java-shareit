package ru.practicum.shareit.exception;

public class NotFoundByIdException extends RuntimeException {
    public NotFoundByIdException(String message) {
        super(message);
    }
}
