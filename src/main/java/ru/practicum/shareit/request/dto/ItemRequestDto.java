package ru.practicum.shareit.request.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ItemRequestDto {
    private long id;
    private String description; //текст запроса, содержащий описание требуемой вещи
    private long requester; // пользователь, создавший запрос;
    private LocalDateTime created; // дата и время создания запроса
    private List<ItemDto> items;
}
