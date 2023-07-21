package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    ItemDto addItemDto(long id, ItemDto itemDto);

    void updateItem(long id, ItemDto itemDto);

    ItemDto getItemById(long id);

    List<ItemDto> getItems(long userId);

    List<ItemDto> getItemsByDescription(String text);
}
