package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@Data
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    User booker;
    Status status;
}
