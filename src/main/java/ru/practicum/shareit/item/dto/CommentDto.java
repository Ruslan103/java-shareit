package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;
    private long itemId; // id вещи которую бронируют
    private String text;
    private long userId;
    private String name;
    private String authorName;
    private LocalDateTime created;
}
