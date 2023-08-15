package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

public interface BookingService {
    BookingDtoResponse addBookingDto(long userId, BookingDtoRequest bookingDto);
}
