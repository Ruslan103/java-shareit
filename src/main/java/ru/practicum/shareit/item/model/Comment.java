package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "item_id")
    private Long itemId; // id вещи которую бронируют
    @Column(name = "comment")
    private String text;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "item_name")
    private String name;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "created")
    private LocalDateTime created;
}
