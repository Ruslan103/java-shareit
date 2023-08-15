package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoRequest {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId; // id пользователя который бронирует
    private Status status;
    private Long itemId; // id вещи которую бронируют

    public BookingDtoRequest(long id, LocalDateTime start, LocalDateTime end, Long bookerId, Status status, Long itemId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.bookerId = bookerId;
        this.status = status;
        this.itemId = itemId;
    }

    public BookingDtoRequest() {
    }
}
