package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDtoResponse addBooking(long userId, BookingDtoRequest bookingDto) {
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundByIdException("Item by id not found");
        }
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        if (userId == item.getOwner()) {
            throw new NotFoundByIdException("The owner of the item cannot book it");
        }
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("Item unavailable");//400
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundByIdException("User by id not found");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new LineNotNullException("Start booking or end booking can not be null");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingTimeException("The end or tne start of the booking cannot be in the past");//400
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingTimeException("Incorrect end time of booking"); //400
        }
        bookingDto.setBookerId(userId);
        bookingDto.setStatus(Status.WAITING);
        Booking booking = BookingMapper.toBooking(userRepository, itemRepository, bookingDto);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoResponse(booking);
    }

    public BookingDtoResponse updateBooking(long userId, long bookingId, Boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        Item item = booking.getItem();
        if (approved && booking.getStatus() == Status.APPROVED) {
            throw new StatusApprovedException("Status already approved");
        }
        if (userId == item.getOwner() && approved) {
            booking.setStatus(Status.APPROVED);
            // return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
        } else if (userId == item.getOwner() && !approved) {
            booking.setStatus(Status.REJECTED);
            // return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
        } else {
            throw new NotFoundByIdException("This user is not owner");
        }
        return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    public BookingDtoResponse getBookingById(long bookingId, long userId) {
        try {
            return BookingMapper.toBookingDtoResponse(bookingRepository.findBookingByIdAndUser(bookingId, userId));
        } catch (NullPointerException e) {
            throw new NotFoundByIdException("Booking not found for the user");
        }
    }

    public List<BookingDtoResponse> findBookingsByBookerAndStatus(long bookerId, String state) {

        if (!userRepository.existsById(bookerId)) {
            throw new NotFoundByIdException("User by id not found");
        }
        User booker = userRepository.getReferenceById(bookerId);
        List<Booking> bookings;
        if (state.equalsIgnoreCase("All")) {
            bookings = bookingRepository.findBookingsByBooker(booker);
        } else if (state.equalsIgnoreCase("FUTURE")) {
            List<Status> statusList = List.of(Status.APPROVED, Status.WAITING);
            bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, statusList);
        } else {
            throw new ValidationException("Unknown state: " + state);
        }
        return BookingMapper.bookingDtoResponseList(SortBookingByStart.sort(bookings));
    }

    public List<BookingDtoResponse> findBookingsByOwnerAndStatus(long ownerId, String state) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundByIdException("User by id not found");
        }

        List<Booking> bookings;
        if (state.equalsIgnoreCase("All")) {
            bookings = bookingRepository.findBookingsByOwner(ownerId);
        } else if (state.equalsIgnoreCase("FUTURE")) {
            List<Status> statusList = List.of(Status.APPROVED, Status.WAITING);
            bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, statusList);
        } else {
            throw new ValidationException("Unknown state: " + state);
        }
        return BookingMapper.bookingDtoResponseList(SortBookingByStart.sort(bookings));
    }

    private static class SortBookingByStart {
        private static List<Booking> sort(List<Booking> bookings) {
            bookings.sort(Comparator.comparing(Booking::getStart).reversed());
            return bookings;
        }
    }
}
