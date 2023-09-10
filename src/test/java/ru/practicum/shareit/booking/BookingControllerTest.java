package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private Booking booking1;
    private Booking booking2;

    private BookingDtoRequest bookingDtoRequest;
    private BookingDtoResponse bookingDto;
    private PageRequest pageRequest;

    @BeforeEach
    void beforeEach() {
        pageRequest = PageRequest.of(0, 5, Sort.by("start").descending());
        User user1 = User.builder()
                .id(1)
                .name("NameUser1")
                .email("user1@email.com")
                .build();

        User user2 = User.builder()
                .id(2)
                .name("NameUser2")
                .email("user2@email.com")
                .build();

        User user3 = User.builder()
                .id(3)
                .name("NameUser3")
                .email("user3@email.com")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .name("NameItem1")
                .description("Description for item1")
                .available(true)
                .owner(user1)
                .build();

        Item item2 = Item.builder()
                .id(2)
                .name("NameItem2")
                .description("Description for item2")
                .available(true)
                .owner(user2)
                .build();

        booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusMinutes(15))
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
        bookingDto = BookingMapper.toBookingDtoResponse(booking1);
        bookingDtoRequest = BookingDtoRequest.builder()
                .id(1)
                .bookerId(3L)
                .itemId(1L)
                .start(booking1.getStart())
                .end(booking1.getEnd())
                .build();

    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.addBooking(anyLong(), any(BookingDtoRequest.class))).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).addBooking(3, bookingDtoRequest);
    }

    @Test
    void updateBookingTest() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).updateBooking(1L, 1L, true);
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).getBookingById(1, 1);
    }

    @Test
    void findBookingsByBookerAndStatusTest() throws Exception {
        when(bookingService.findBookingsByBookerAndStatus(anyLong(), anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            List<Booking> bookings = new ArrayList<>();
            bookings.add(booking1);
            bookings.add(booking2);
            return bookings;
        });
        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(booking1, booking2))));
    }

    @Test
    void findBookingsByOwnerAndStatusTest() throws Exception {
        when(bookingService.findBookingsByOwnerAndStatus(anyLong(), anyString(), anyInt(), anyInt())).thenAnswer(invocation -> {
            List<Booking> bookings = new ArrayList<>();
            bookings.add(booking1);
            bookings.add(booking2);
            return bookings;
        });
        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(booking1, booking2))));
    }
}