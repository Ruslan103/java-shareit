package ru.practicum.shareit.item.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto addItemDto(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);

        if (item.getName().isEmpty()) {
            throw new LineNotNullException("The name cannot be empty");
        }
        if (item.getDescription() == null) {
            throw new LineNotNullException("The description cannot be empty");
        }
        if (item.getAvailable() == null) {
            throw new LineNotNullException("The available cannot be empty");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundByIdException("User by id not found"); // 404
        }
        item.setOwner(userId);
        return ItemMapper.itemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (itemRepository.existsById(itemId) && userRepository.existsById(userId)) {
            Item oldItem = itemRepository.getReferenceById(itemId);
            Item updateItem = Item.builder()
                    .name(item.getName() != null ? item.getName() : oldItem.getName())
                    .owner(item.getOwner() != null ? item.getOwner() : oldItem.getOwner())
                    .available(item.getAvailable() != null ? item.getAvailable() : true)
                    .id(oldItem.getId())
                    .description(item.getDescription() != null ? item.getDescription() : oldItem.getDescription())
                    .build();
            return ItemMapper.itemDto(itemRepository.save(updateItem));

        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public ItemDto getItemById(long itemId) {
        if (itemRepository.existsById(itemId)) {
            return ItemMapper.itemDto(itemRepository.getReferenceById(itemId));
        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public List<ItemDto> getItems(long userId) {
        return ItemMapper.getItemDtoList(itemRepository.getItemByOwner(userId));
    }

    public List<ItemDto> getItemsByDescription(String text) {
        List<Item> items = !text.isEmpty()
                ? itemRepository.findByDescriptionContainingIgnoreCase(text).stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList())
                : new ArrayList<>();
        return ItemMapper.getItemDtoList(items);
    }

    public void deleteItemById(long itemId) {
        itemRepository.deleteById(itemId);
    }
}
