package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.ValidationException;

public enum State {
    ALL,
    FUTURE,
    WAITING,
    REJECTED,
    CURRENT,
    PAST;

    public static State getEnum(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new ValidationException("Unknown state: " + state);
        }
    }
}
