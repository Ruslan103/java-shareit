package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItemDto(@RequestHeader("X-Sharer-User-Id") long id, @RequestBody ItemDto itemDto) {
        return itemService.addItemDto(id, itemDto);
    }

    @PatchMapping("{itemId}")
    public void updateItem(@RequestHeader("X-Sharer-User-Id") long id, @RequestBody ItemDto itemDto) {
        itemService.updateItem(id, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("search?text={text}")
    public List<ItemDto> getItemsByDescription(@PathVariable String text) {
        return itemService.getItemsByDescription(text);
    }
}
