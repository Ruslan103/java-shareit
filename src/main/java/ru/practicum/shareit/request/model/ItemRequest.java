package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

// класс, отвечающий за запрос вещи
@Builder
@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "description")
    private String description; //текст запроса, содержащий описание требуемой вещи
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester; // пользователь, создавший запрос;
    @Column(name = "created")
    private LocalDateTime created; // дата и время создания запроса
}
