package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class ItemDbStorage implements ItemStorage {

    private final UserDbStorage userDbStorage;
    private final HashMap<Long, List<Item>> items = new HashMap<>(); // id владельца и список вещей

    private long itemId;

    @Autowired
    public ItemDbStorage(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public Item addItem(long userId, Item item) {
        User user = userDbStorage.getUserById(userId);
        if (!userDbStorage.getUsers().contains(user)) {
            throw new NotFoundByIdException("User by id not found"); // 404
        }
        if (item.getName().isEmpty()) {
            throw new LineNotNullException("The name cannot be empty");
        }
        if (item.getDescription() == null) {
            throw new LineNotNullException("The description cannot be empty");
        }
        if (item.getAvailable() == null) {
            throw new LineNotNullException("The available cannot be empty");
        }
        List<Item> listItems = getItems(userId);
        listItems.add(item);
        itemId++;
        items.put(userId, listItems);
        item.setId(itemId);
        return item;
    }

    public Item updateItem(long userId, long itemId, Item item) {
        List<Item> listItems = items.get(userId);
        Item oldItem = getItemById(itemId);
        if (listItems != null && listItems.contains(oldItem)) {
            listItems.removeIf(newItem -> newItem.getId() == itemId);
            Item updateItem = Item.builder()
                    .name(item.getName() != null ? item.getName() : oldItem.getName())
                    .owner(item.getOwner() != null ? item.getOwner() : oldItem.getOwner())
                    .available(item.getAvailable() != null ? item.getAvailable() : true)
                    .id(oldItem.getId())
                    .description(item.getDescription() != null ? item.getDescription() : oldItem.getDescription())
                    .build();
            listItems.add(updateItem);
            items.put(userId, listItems);
            return updateItem;
        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public Item getItemById(long itemId) {
        Collection<List<Item>> listItems = items.values();
        for (List<Item> items : listItems) {
            for (Item item : items) {
                if (item.getId() == itemId) {
                    return item;
                }
            }
        }
        return null;
    }

    public List<Item> getItems(long userId) {
        return items.getOrDefault(userId, new ArrayList<>());
    }

    public List<Item> getItemsByDescription(String text) {
        Collection<List<Item>> listItems = items.values();
        if (!text.isEmpty()) {
            List<Item> result = new ArrayList<>();
            for (List<Item> items : listItems) {
                for (Item item : items) {
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
