package ru.practicum.exception;

public class BookingTimeException extends RuntimeException {
    public BookingTimeException(String message) {
        super(message);
    }
}
