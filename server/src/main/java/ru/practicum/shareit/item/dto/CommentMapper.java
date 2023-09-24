package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .userId(comment.getUserId())
                .name(comment.getName())
                .authorName(comment.getAuthorName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(long itemId, long userId, CommentDto commentDto) {
        return Comment.builder()
                .userId(userId)
                .itemId(itemId)
                .text(commentDto.getText())
                .name(commentDto.getName())
                .authorName(commentDto.getAuthorName())
                .created(commentDto.getCreated())
                .build();
    }
}
