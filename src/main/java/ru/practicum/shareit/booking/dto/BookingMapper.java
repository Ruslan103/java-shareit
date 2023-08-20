package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
//@Component
public class BookingMapper {
    //    private final ItemRepository itemRepository;
//    private final UserRepository userRepository;
//
//    public BookingMapper(ItemRepository itemRepository, UserRepository userRepository) {
//        this.itemRepository = itemRepository;
//        this.userRepository = userRepository;
//    }


    public static BookingDtoRequest toBookingDtoRequest(Booking booking) {

        return BookingDtoRequest.builder()
                .bookerId(booking.getBooker().getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .build();
    }

    public static Booking toBooking(UserRepository userRepository, ItemRepository itemRepository, BookingDtoRequest bookingDto) {
        return Booking.builder()
                .booker(userRepository.getReferenceById(bookingDto.getBookerId()))
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .id(bookingDto.getId())
                .item(itemRepository.getReferenceById(bookingDto.getItemId()))
                .build();
    }

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        return BookingDtoResponse.builder()
                .booker(booking.getBooker())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .id(booking.getId())
                .item(booking.getItem())
                .build();
    }

    public static List<BookingDtoResponse> bookingDtoResponseList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }

    public static LastAndNextBookingDto toLastAndNextBookingDto(Booking booking) {
        return LastAndNextBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
