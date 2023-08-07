package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService bookingService;
    public BookingDto addBookingDto(BookingDto bookingDto){
      return bookingService.addBookingDto(bookingDto);
    }
}
