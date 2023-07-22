package ru.practicum.shareit.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) { //500
        super(message);
    }
}
