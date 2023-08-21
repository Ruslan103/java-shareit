package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto addItemDto(@RequestHeader("X-Sharer-User-Id") long id, @RequestBody ItemDto itemDto) {
        return itemService.addItemDto(id, itemDto);
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("{id}")
    public ItemDto getItemById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByDescription(@RequestParam(value = "text", defaultValue = "") String text) {
        return itemService.getItemsByDescription(text);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody Comment comment) {
        return commentService.addComment(userId, id, comment);
    }
}
