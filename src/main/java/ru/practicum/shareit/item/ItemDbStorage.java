package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class ItemDbStorage implements ItemStorage {

    private final UserDbStorage userDbStorage;
    private final HashMap<Long, List<Item>> userItems = new HashMap<>(); // id владельца и список вещей
    private final HashMap<Long, Item> items = new HashMap<>(); // id предмета и предмет

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
        itemId++;
        item.setId(itemId);
        listItems.add(item);
        items.put(itemId, item);
        userItems.put(userId, listItems);
        return item;
    }

    public Item updateItem(long userId, long itemId, Item item) {
        List<Item> listItems = userItems.get(userId);
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
            items.put(itemId, updateItem);
            userItems.put(userId, listItems);
            return updateItem;
        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    public List<Item> getItems(long userId) {
        return userItems.getOrDefault(userId, new ArrayList<>());
    }

    public List<Item> getItemsByDescription(String text) {
        Collection<Item> listItems = items.values();
        if (!text.isEmpty()) {
            List<Item> result = new ArrayList<>();
            for (Item item : listItems) {
                String description = item.getDescription().toLowerCase();
                if (description.contains(text.toLowerCase()) && item.getAvailable()) {
                    result.add(item);
                }
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
