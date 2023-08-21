package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoRequest {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId; // id пользователя который бронирует
    private Status status;
    private Long itemId; // id вещи которую бронируют
}
