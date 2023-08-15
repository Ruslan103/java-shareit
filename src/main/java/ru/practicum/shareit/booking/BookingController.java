package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
  private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse addBookingDto(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDtoRequest bookingDto) {
        return bookingService.addBookingDto(userId, bookingDto); // метод принимает id пользователя который бронирует и бронь
    }
}
