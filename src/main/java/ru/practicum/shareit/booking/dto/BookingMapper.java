package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static BookingDto bookingDto(Booking bookingDtbooking) {
        return BookingDto.builder()
                .booker(bookingDtbooking.getBooker())
                .end(bookingDtbooking.getEnd())
                .start(bookingDtbooking.getStart())
                .status(bookingDtbooking.getStatus())
                .id(bookingDtbooking.getId())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .booker(bookingDto.getBooker())
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .id(bookingDto.getId())
                .build();
    }
}
