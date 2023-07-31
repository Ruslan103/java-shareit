package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(long id, Item item);

    Item updateItem(long userId, long itemId, Item item);

    Item getItemById(long itemId);

    List<Item> getItems(long userId);

    List<Item> getItemsByDescription(String text);
}
