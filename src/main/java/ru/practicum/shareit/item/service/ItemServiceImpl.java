package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    public ItemDto addItemDto(long id, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.itemDto(itemStorage.addItem(id, item));
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.itemDto(itemStorage.updateItem(userId, itemId, item));
    }

    public ItemDto getItemById(long id) {
        return ItemMapper.itemDto(itemStorage.getItemById(id));
    }

    public List<ItemDto> getItems(long userId) {
        List<Item> items = itemStorage.getItems(userId);
        return ItemMapper.getItemDtoList(items);
    }

    public List<ItemDto> getItemsByDescription(String text) {
        List<Item> items = itemStorage.getItemsByDescription(text);
        return ItemMapper.getItemDtoList(items);
    }
}
