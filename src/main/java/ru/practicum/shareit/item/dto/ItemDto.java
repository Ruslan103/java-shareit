package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId; // владелец вещи
    private Long requestId; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос
    private LastAndNextBookingDto lastBooking;
    private LastAndNextBookingDto nextBooking;
    private List<Comment> comments;
}
