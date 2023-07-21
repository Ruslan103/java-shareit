package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;
    public ItemDto addItemDto(long id, ItemDto itemDto){
        return itemStorage.addItemDto(id,itemDto);
    }

    public void updateItem(long id, ItemDto itemDto){
        itemStorage.updateItem(id,itemDto);
    }

    public ItemDto getItemById(long id){
       return itemStorage.getItemById(id);
    }

    public List<ItemDto> getItems(long userId){
        return itemStorage.getItems(userId);
    }

    public List<ItemDto> getItemsByDescription(String text){
        return itemStorage.getItemsByDescription(text);
    }
}
