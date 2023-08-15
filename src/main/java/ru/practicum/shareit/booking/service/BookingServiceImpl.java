package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BookingTimeException;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDtoResponse addBookingDto(long userId, BookingDtoRequest bookingDto) {

        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundByIdException("Item by id not found");
        }
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("Item unavailable");//400
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundByIdException("User by id not found");
        }
        if (bookingDto.getStart()==null||bookingDto.getEnd()==null){
            throw new LineNotNullException("Start booking or end booking can not be null");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())||bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingTimeException("The end or tne start of the booking cannot be in the past");//400
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())||bookingDto.getStart().equals(bookingDto.getEnd())){
            throw new BookingTimeException("Incorrect end time of booking"); //400
        }
        User booker = userRepository.getReferenceById(userId);
      //  Item itemBooking = itemRepository.getReferenceById(bookingDto.getItemId());
        bookingDto.setBookerId(userId);
        bookingDto.setStatus(Status.WAITING);
        Booking booking = BookingMapper.toBooking(userRepository,itemRepository,bookingDto);
        //booking.setBookerId(userId);
       // booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoResponse(booking);
    }
}
