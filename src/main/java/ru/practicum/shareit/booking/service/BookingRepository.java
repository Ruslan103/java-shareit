package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface BookingRepository extends JpaRepository <Booking,Long> {
    List<Booking> findBookingsByBookerAndStatus(User booker, Status status);
    List<Booking> findBookingsByBookerAndStatusIsIn(User booker, List<Status> statuses);
    List<Booking> findBookingsByBooker(User booker);
}
