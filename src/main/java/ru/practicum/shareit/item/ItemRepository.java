package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {
    Item getItemByIdAndOwner(long itemId,long ownerId);
    List <Item> getItemByOwner(long userId);
    List <Item> findByDescriptionContainingIgnoreCase(String text);
}
