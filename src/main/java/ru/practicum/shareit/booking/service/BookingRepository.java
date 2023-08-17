package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerAndStatus(User booker, Status status);

    List<Booking> findBookingsByBookerAndStatusIsIn(User booker, List<Status> statuses);

    List<Booking> findBookingsByBooker(User booker);

    @Query("SELECT b FROM Booking b WHERE b.id = ?1 and ( b.item.owner = ?2 or b.booker.id = ?2)")
    Booking findBookingByIdAndUser(long bookingId, long userId);
    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1")
    List<Booking> findBookingsByOwner(Long owner);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 and  b.status IN ?2")
    List<Booking> findBookingsByOwnerAndStatus(Long owner, List<Status> statuses);
}
