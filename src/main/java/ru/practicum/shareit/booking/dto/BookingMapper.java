package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static BookingDto bookingDto(BookingDto bookingDto) {
        return BookingDto.builder()
                .booker(bookingDto.getBooker())
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .id(bookingDto.getId())
                .build();
    }

    public static Booking toBooking(Booking booking) {
        return Booking.builder()
                .booker(booking.getBooker())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .id(booking.getId())
                .build();
    }
}
