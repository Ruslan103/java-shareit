package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItemDto(long id, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItemById(long id,long userId);

    List<ItemDto> getItems(long userId);

    List<ItemDto> getItemsByDescription(String text);
}
