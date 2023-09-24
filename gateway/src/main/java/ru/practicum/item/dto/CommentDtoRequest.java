package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    private Long id;
    @NotBlank(message = "Comment text missing")
    private String text;
    private String authorName;
    private LocalDateTime created;
}
