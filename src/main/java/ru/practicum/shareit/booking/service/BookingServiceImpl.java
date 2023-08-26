package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingDtoResponse addBooking(long userId, BookingDtoRequest bookingDto) {
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundByIdException("Item by id not found");
        }
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        if (userId == item.getOwner().getId()) {
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

    @Transactional
    public BookingDtoResponse updateBooking(long userId, long bookingId, Boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        Item item = booking.getItem();
        if (approved && booking.getStatus() == Status.APPROVED) {
            throw new StatusApprovedException("Status already approved");
        }
        if (userId == item.getOwner().getId() && approved) {
            booking.setStatus(Status.APPROVED);
        } else if (userId == item.getOwner().getId() && !approved) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new NotFoundByIdException("This user is not owner");
        }
        return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingDtoResponse getBookingById(long bookingId, long userId) {
        try {
            return BookingMapper.toBookingDtoResponse(bookingRepository.findBookingByIdAndUser(bookingId, userId));
        } catch (NullPointerException e) {
            throw new NotFoundByIdException("Booking not found for the user");
        }
    }

    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findBookingsByBookerAndStatus(long bookerId, String state) {
        if (!userRepository.existsById(bookerId)) {
            throw new NotFoundByIdException("User by id not found");
        }
        User booker = userRepository.getReferenceById(bookerId);
        List<Booking> bookings = null;
        State enumState = State.getEnum(state);
        switch (enumState) {
            case ALL:
                bookings = bookingRepository.findBookingsByBooker(booker);
                break;
            case FUTURE:
                List<Status> futureStatusList = List.of(Status.APPROVED, Status.WAITING);
                bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, futureStatusList);
                break;
            case WAITING:
                List<Status> waitingStatusList = List.of(Status.WAITING);
                bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, waitingStatusList);
                break;
            case REJECTED:
                List<Status> rejectedStatusList = List.of(Status.REJECTED);
                bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, rejectedStatusList);
                break;
            case CURRENT:
                List<Status> currentStatusList = List.of(Status.REJECTED, Status.APPROVED);
                bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, currentStatusList)
                        .stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case PAST:
                List<Status> pastStatusList = List.of(Status.APPROVED);
                bookings = bookingRepository.findBookingsByBookerAndStatusIsIn(booker, pastStatusList)
                        .stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
        }

        return BookingMapper.bookingDtoResponseList(SortBookingByStart.sort(bookings));
    }

    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findBookingsByOwnerAndStatus(long ownerId, String state) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundByIdException("User by id not found");
        }
        List<Booking> bookings = null;
        State enumState = State.getEnum(state);
        switch (enumState) {
            case ALL:
                bookings = bookingRepository.findBookingsByOwner(ownerId);
                break;
            case FUTURE:
                List<Status> futureStatusList = List.of(Status.APPROVED, Status.WAITING);
                bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, futureStatusList);
                break;
            case WAITING:
                List<Status> waitingStatusList = List.of(Status.WAITING);
                bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, waitingStatusList);
                break;
            case REJECTED:
                List<Status> rejectedStatusList = List.of(Status.REJECTED);
                bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, rejectedStatusList);
                break;
            case CURRENT:
                List<Status> currentStatusList = List.of(Status.REJECTED, Status.APPROVED);
                bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, currentStatusList)
                        .stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case PAST:
                List<Status> pastStatusList = List.of(Status.APPROVED);
                bookings = bookingRepository.findBookingsByOwnerAndStatus(ownerId, pastStatusList)
                        .stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
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
