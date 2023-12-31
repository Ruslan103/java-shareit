package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerAndStatusIsIn(User booker, List<Status> statuses, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 " +
            "FROM Booking b " +
            "WHERE b.booker = ?1 " +
            "AND b.item = ?2 " +
            "AND b.status IN ?3")
    boolean isBookerAndItemExist(User booker, Item item, List<Status> statuses);

    List<Booking> findBookingsByBooker(User booker, Pageable pageable);

    Booking findTopByBookerAndItemOrderByStart(User booker, Item item);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.id = ?1 and ( b.item.owner.id = ?2 or b.booker.id = ?2)")
    Booking findBookingByIdAndUser(long bookingId, long userId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1")
    List<Booking> findBookingsByOwner(long owner, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 and  b.status IN ?2")
    List<Booking> findBookingsByOwnerAndStatus(long owner, List<Status> statuses, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 AND b.status IN ?2 ")
    List<Booking> findBookingByItem1(long itemId, List<Status> statuses);

    List<Booking> findBookingByItem(Item itemI);

    Booking findFirstByItemAndStatusIsInAndStartBeforeOrderByStartDesc(Item itemId, List<Status> statuses, LocalDateTime start);

    Booking findFirstByItemAndStatusIsInAndEndAfterOrderByStartAsc(Item itemId, List<Status> statuses, LocalDateTime end);

    Booking findFirstByItemAndStatusInAndStartBefore(Item item, List<Status> statuses, LocalDateTime start);
}
