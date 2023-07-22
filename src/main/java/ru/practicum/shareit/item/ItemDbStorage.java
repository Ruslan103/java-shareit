package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
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
    private final HashMap<Long, List<ItemDto>> itemsDto = new HashMap<>(); // id владельца и список вещей

    long itemId;

    @Autowired
    public ItemDbStorage(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public ItemDto addItemDto(long userId, ItemDto itemDto) {
        User user = userDbStorage.getUserById(userId);
        if (!userDbStorage.getUsers().contains(user)) {
            throw new NotFoundByIdException("User by id not found"); // 404
        }
        if (itemDto.getName().isEmpty()) {
            throw new LineNotNullException("The name cannot be empty");
        }
        if (itemDto.getDescription() == null) {
            throw new LineNotNullException("The description cannot be empty");
        }
        if (itemDto.getAvailable() == null) {
            throw new LineNotNullException("The available cannot be empty");
        }
        List<ItemDto> listItems = getItems(userId);
        listItems.add(itemDto);
        itemId++;
        itemsDto.put(userId, listItems);
        itemDto.setId(itemId);
        return itemDto;
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        List<ItemDto> listItems = itemsDto.get(userId);
        ItemDto oldItem = getItemById(itemId);
        if (listItems != null && listItems.contains(oldItem)) {
            listItems.removeIf(item -> item.getId() == itemId);
            ItemDto updateItem = ItemDto.builder()
                    .name(itemDto.getName() != null ? itemDto.getName() : oldItem.getName())
                    .owner(itemDto.getOwner() != null ? itemDto.getOwner() : oldItem.getOwner())
                    .available(itemDto.getAvailable() != null ? itemDto.getAvailable() : true)
                    .id(oldItem.getId())
                    .description(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription())
                    .build();
            listItems.add(updateItem);
            itemsDto.put(userId, listItems);
            return updateItem;
        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public ItemDto getItemById(long itemId) {
        Collection<List<ItemDto>> listItems = itemsDto.values();
        for (List<ItemDto> items : listItems) {
            for (ItemDto item : items) {
                if (item.getId() == itemId) {
                    return item;
                }
            }
        }
        return null;
    }

    public List<ItemDto> getItems(long userId) {
        return itemsDto.getOrDefault(userId, new ArrayList<>());
    }

    public List<ItemDto> getItemsByDescription(String text) {
        Collection<List<ItemDto>> listItems = itemsDto.values();
        if (!text.isEmpty()) {
            List<ItemDto> result = new ArrayList<>();
            for (List<ItemDto> items : listItems) {
                for (ItemDto item : items) {
                    String description = item.getDescription().toLowerCase();
                    if (description.contains(text.toLowerCase()) && item.getAvailable()) {
                        result.add(item);
                    }
                }
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
