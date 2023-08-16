package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse addBooking(long userId, BookingDtoRequest bookingDto);
    BookingDtoResponse updateBooking(long userId, long bookingId, Boolean approved);
    BookingDtoResponse getBookingById(long bookingId);
    List<BookingDtoResponse> findBookingsByBookerAndStatus (long bookerId, String state);
}
