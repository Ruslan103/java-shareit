package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
// класс, отвечающий за запрос вещи
    @Data
    @Builder
public class ItemRequest {
    private long id;
    private String description; //текст запроса, содержащий описание требуемой вещи
    private User requestor; // пользователь, создавший запрос;
    private LocalDateTime created; // дата и время создания запроса
}
