package ru.practicum.shareit.exception;

public class UserByIdNotFoundException extends RuntimeException{
    public UserByIdNotFoundException(String message) {
        super(message);
    }
}
