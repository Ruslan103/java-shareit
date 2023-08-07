package ru.practicum.shareit.item.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto addItemDto(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (!userRepository.existsById(userId)) {
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
        item.setOwner(userId);
       // itemRepository.save(item);
        return ItemMapper.itemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (itemRepository.existsById(itemId)) {
            item.setId(itemId);
            return ItemMapper.itemDto(itemRepository.save(item));

        } else {
            throw new NotFoundByIdException("Item not found");//404
        }
    }

    public ItemDto getItemById(long id) {

        return ItemMapper.itemDto(itemRepository.getReferenceById(id));
    }

    public List<ItemDto> getItems(long userId) {
        return ItemMapper.getItemDtoList(itemRepository.getItemByOwner(userId));
    }

    public List<ItemDto> getItemsByDescription(String text) {
        return ItemMapper.getItemDtoList(itemRepository.findByDescriptionContaining(text));
    }
    public void deleteItemById(long itemId){
       itemRepository.deleteById(itemId);
    }
}
