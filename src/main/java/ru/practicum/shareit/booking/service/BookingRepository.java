package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.Status.APPROVED;

public interface BookingRepository extends JpaRepository<Booking, Long> {
//    List<Booking> findBookingsByBookerAndStatus(User booker, Status status);

    List<Booking> findBookingsByBookerAndStatusIsIn(User booker, List<Status> statuses);

    List<Booking> findBookingsByBooker(User booker);

    @Query("SELECT b FROM Booking b WHERE b.id = ?1 and ( b.item.owner = ?2 or b.booker.id = ?2)")
    Booking findBookingByIdAndUser(long bookingId, long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1")
    List<Booking> findBookingsByOwner(long owner);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 and  b.status IN ?2")
    List<Booking> findBookingsByOwnerAndStatus(long owner, List<Status> statuses);

//    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 and b.item = ?2 ORDER BY b.id DESC")
//    List<Booking> findBookingsByOwnerAndItem(long owner, Item item);
//
//    @Query("SELECT b FROM Booking b WHERE b.item.id= ?1 and b.end <= ?2 ORDER BY b.end DESC")
//    List<Booking> findLostBooking (long itemId, LocalDateTime localDateTime);
    @Query("SELECT b FROM Booking b WHERE b.item.id= ?1 AND b.status IN ?2  ORDER BY b.start DESC")
    List<Booking> findBookingByItem (long itemId,List<Status> statuses);
}
