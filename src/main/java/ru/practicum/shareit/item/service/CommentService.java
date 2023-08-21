package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public interface CommentService {
    CommentDto addComment(long userId, long itemId, Comment comment);
}
