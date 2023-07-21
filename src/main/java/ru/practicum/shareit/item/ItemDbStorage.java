package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.UserByIdNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class ItemDbStorage implements ItemStorage {


    private final UserDbStorage userDbStorage;
    HashMap<Long, List<ItemDto>> itemsDto = new HashMap<>(); // id владельца и список вещей

    @Autowired
    public ItemDbStorage(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public ItemDto addItemDto(long id, ItemDto itemDto) {
        User user = userDbStorage.getUserById(id);
        if (!userDbStorage.getUsers().contains(user)) {
            throw new UserByIdNotFoundException("Пользователь по id не найден"); // 404
        }
        if (itemDto.getName().isEmpty()){
            throw new LineNotNullException("Имя не может быть пустым");
        }
        List<ItemDto> listItems = itemsDto.get(id);
        if (listItems != null) {
            listItems.add(itemDto);
        }
        itemsDto.put(id, listItems);
        itemDto.setId(id);
        return itemDto;
    }

    public void updateItem(long id, ItemDto itemDto) {
        List<ItemDto> listItems = itemsDto.get(id);
        listItems.removeIf(item -> item.getId() == itemDto.getId());
        listItems.add(itemDto);
        itemsDto.put(id, listItems);
    }

    public ItemDto getItemById(long id) {
        Collection<List<ItemDto>> listItems = itemsDto.values();
        for (List<ItemDto> items : listItems) {
            for (ItemDto item : items) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public List<ItemDto> getItems(long userId) {
        return itemsDto.get(userId);
    }

    public List<ItemDto> getItemsByDescription(String text) {
        Collection<List<ItemDto>> listItems = itemsDto.values();
        List<ItemDto> result = new ArrayList<>();
        for (List<ItemDto> items : listItems) {
            for (ItemDto item : items) {
                if (item.getDescription().contains(text)) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
