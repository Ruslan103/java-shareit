package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {
    private final BookingRepository bookingRepository;

    public BookingDto addBookingDto(BookingDto bookingDto) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        return BookingMapper.bookingDto(bookingRepository.save(booking));
    }
}
