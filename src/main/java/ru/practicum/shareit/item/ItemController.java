package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

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
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId,@RequestHeader ("X-Sharer-User-Id") long userId) {
        ItemDto itemDto = itemService.getItemById(itemId,userId);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("search")
    public List<ItemDto> getItemsByDescription(@RequestParam(value = "text", defaultValue = "") String text) {
        return itemService.getItemsByDescription(text);
    }
}
