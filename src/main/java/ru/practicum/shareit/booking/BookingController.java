package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse addBookingDto(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody BookingDtoRequest bookingDto) {
        return bookingService.addBooking(userId, bookingDto); // метод принимает id пользователя который бронирует и бронь
    }

    @PatchMapping ("/{bookingId}")
    public BookingDtoResponse updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam(value = "approved") Boolean approved) {
        return bookingService.updateBooking(userId,bookingId,approved);
    }
    @GetMapping("/{bookingId}")
   public BookingDtoResponse getBookingById(@PathVariable long bookingId){
        return bookingService.getBookingById(bookingId);
    }
    @GetMapping
    public List<BookingDtoResponse> findBookingByBookerId(@RequestHeader long bookerId){
        return bookingService.findBookingByBookerId(bookerId);
    }
}
