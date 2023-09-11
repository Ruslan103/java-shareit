package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private ItemDto itemDto;
    private Booking booking1;
    private Booking booking2;

    private BookingDtoRequest bookingDtoRequest;
    private PageRequest pageRequest;

    @BeforeEach
    void beforeEach() {
        pageRequest = PageRequest.of(0, 5);
        user1 = User.builder()
                .id(1)
                .name("NameUser1")
                .email("user1@email.com")
                .build();

        user2 = User.builder()
                .id(2)
                .name("NameUser2")
                .email("user2@email.com")
                .build();

        user3 = User.builder()
                .id(3)
                .name("NameUser3")
                .email("user3@email.com")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1)
                .description("Description")
                .created(LocalDateTime.now())
                .build();

        item1 = Item.builder()
                .id(1)
                .name("NameItem1")
                .description("Description for item1")
                .available(true)
                .owner(user1)
                .request(itemRequest)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("NameItem2")
                .description("Description for item2")
                .available(true)
                .owner(user2)
                .build();


        itemDto = ItemMapper.itemDto(item1);
        ItemDto itemDto2 = ItemMapper.itemDto(item2);

        Comment comment = Comment.builder()
                .id(1)
                .authorName(user1.getName())
                .created(LocalDateTime.now())
                .text("text")
                .build();
        booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(30))
                .item(item1)
                .booker(user3)
                .status(Status.WAITING)
                .build();

        booking2 = Booking.builder()
                .id(2)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item1)
                .booker(user3)
                .status(Status.APPROVED)
                .build();

        bookingDtoRequest = BookingMapper.toBookingDtoRequest(booking1);
    }

    @Test
    void addBooking() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item2);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);
        BookingDtoResponse bookingDtoResponse = bookingService.addBooking(user1.getId(), bookingDtoRequest);
        assertEquals(booking1.getId(), bookingDtoResponse.getId());
    }

    @Test
    void updateBooking() {

        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking1);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);
        BookingDtoResponse bookingDtoResponse = bookingService.updateBooking(1, 1, true);
        booking1.setStatus(Status.APPROVED);
        assertEquals(bookingDtoResponse.getStatus(), Status.APPROVED);
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findBookingByIdAndUser(1, 1)).thenReturn(booking1);
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(1, 1);
        assertEquals(bookingDtoResponse.getId(), booking1.getId());
    }

    @Test
    void findBookingsByBookerAndStatus() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        when(bookingRepository.findBookingsByBooker(any(User.class), any(Pageable.class))).thenReturn(List.of(booking1, booking2));
        List<BookingDtoResponse> bookingDtoResponseList = bookingService.findBookingsByBookerAndStatus(1, "ALL", 1, 5);
        List<Booking> bookingDtoResponseListTest = List.of(booking1, booking2);
        assertEquals(bookingDtoResponseList.size(), bookingDtoResponseListTest.size());
        assertEquals(bookingDtoResponseList.get(0).getId(), bookingDtoResponseListTest.get(0).getId());
        assertEquals(bookingDtoResponseList.get(1).getId(), bookingDtoResponseListTest.get(1).getId());
    }

    @Test
    void findBookingsByOwnerAndStatus() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findBookingsByOwner(anyLong(), any(Pageable.class))).thenReturn(List.of(booking1, booking2));
        List<BookingDtoResponse> bookingDtoResponseList = bookingService.findBookingsByOwnerAndStatus(1, "ALL", 1, 5);
        List<Booking> bookingDtoResponseListTest = List.of(booking1, booking2);
        assertEquals(bookingDtoResponseListTest.size(), bookingDtoResponseList.size());
        assertEquals(bookingDtoResponseList.get(0).getId(), bookingDtoResponseListTest.get(0).getId());
        assertEquals(bookingDtoResponseList.get(1).getId(), bookingDtoResponseListTest.get(1).getId());
    }
}