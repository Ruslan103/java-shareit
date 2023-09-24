package ru.practicum.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) { //500
        super(message);
    }
}
