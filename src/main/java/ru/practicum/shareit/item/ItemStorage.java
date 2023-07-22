package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    ItemDto addItemDto(long id, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItems(long userId);

    List<ItemDto> getItemsByDescription(String text);
}
