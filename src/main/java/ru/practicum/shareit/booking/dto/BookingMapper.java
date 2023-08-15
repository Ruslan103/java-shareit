package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

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

    public static Booking toBooking(UserRepository userRepository,ItemRepository itemRepository, BookingDtoRequest bookingDto) {
//        UserService userService = new UserServiceImpl();
//        final ItemRepository itemRepository = null;
//        final UserRepository userRepository = null;
//        final ItemRepository itemRepository = null;
        return Booking.builder()
                .booker(userRepository.getReferenceById(bookingDto.getBookerId()))
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .id(bookingDto.getId())
                .item(itemRepository.getReferenceById(bookingDto.getItemId()))
                .build();
    }
    public static BookingDtoResponse toBookingDtoResponse (Booking booking) {
        return BookingDtoResponse.builder()
                .booker(booking.getBooker())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .id(booking.getId())
                .item(booking.getItem())
                .build();
    }
//    public static BookingDto2 bookingDto2 (Booking booking) {
//        return BookingDto2.builder()
//                .booker(booking.getBookerId())
//                .end(booking.getEnd())
//                .start(booking.getStart())
//                .status(booking.getStatus())
//                .id(booking.getId())
//                .item(booking.getItem(booking.getItemId()))
//                .build();
//    }
//public static Booking mapToEntity(BookingDto dto) {
//    Booking booking = Booking.builder()
//            .id(dto.getId())
//            .start(dto.getStart())
//            .end(dto.getEnd())
//            .status(dto.getStatus())
//            .build();
//    User booker = UserRepository.findById(dto.getBookerId());
//    booking.setBooker(booker);
//    Item item = ItemRepository.findById(dto.getItemId());
//    booking.setItem(item);
//    return booking;
//}

}
