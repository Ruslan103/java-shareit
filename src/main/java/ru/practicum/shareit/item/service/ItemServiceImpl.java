package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    public ItemDto addItemDto(long id, ItemDto itemDto) {
        return itemStorage.addItemDto(id, itemDto);
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
       return itemStorage.updateItem(userId, itemId, itemDto);
    }

    public ItemDto getItemById(long id) {
        return itemStorage.getItemById(id);
    }

    public List<ItemDto> getItems(long userId) {
        return itemStorage.getItems(userId);
    }

    public List<ItemDto> getItemsByDescription(String text) {
        return itemStorage.getItemsByDescription(text);
    }
}
