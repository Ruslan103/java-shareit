package shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItServer.class, ItemServiceImpl.class, UserServiceImpl.class, BookingServiceImpl.class})
@TestPropertySource(properties = {"db.name=test"})
public class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;

    private User owner;
    private User booker;

    private Item item;

    private Booking booking;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2023, 12, 12, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 12, 15, 0);
        owner = User.builder()
                .id(1)
                .name("Tom")
                .email("cruise@test.com")
                .build();

        booker = User.builder()
                .id(2)
                .name("Tom")
                .email("holland@test.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("XBOX")
                .description("Microsoft gaming console")
                .available(true)
                .owner(owner)
                .build();

        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void addBooking() {
        userService.addUser(UserMapper.userDto(owner));
        userService.addUser(UserMapper.userDto(booker));
        itemService.addItemDto(1, ItemMapper.itemDto(item));

        bookingService.addBooking(booker.getId(), BookingMapper.toBookingDtoRequest(booking));
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking queryBooking = query
                .setParameter("id", 1L)
                .getSingleResult();
        assertNotNull(queryBooking);
        Assertions.assertEquals(Status.WAITING, queryBooking.getStatus());
    }

    @Test
    void getBookingById() {
        userService.addUser(UserMapper.userDto(owner));
        userService.addUser(UserMapper.userDto(booker));
        itemService.addItemDto(2, ItemMapper.itemDto(item));
        bookingService.addBooking(1L, BookingMapper.toBookingDtoRequest(booking));
        BookingDtoResponse result = bookingService.getBookingById(1L, 1L);
        Assertions.assertEquals(Status.WAITING, result.getStatus());
        Assertions.assertEquals(booking.getEnd(), result.getEnd());
    }
}