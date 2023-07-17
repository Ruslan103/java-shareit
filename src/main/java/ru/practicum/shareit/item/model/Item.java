package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available; // статус о том, доступна или нет вещь для аренды
    private User owner; // владелец вещи
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.


}
