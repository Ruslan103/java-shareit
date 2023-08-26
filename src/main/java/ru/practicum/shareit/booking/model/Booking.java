package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_booking")
    private LocalDateTime start;
    @Column(name = "end_booking")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "booker", referencedColumnName = "id")
    private User booker; // пользователь который бронирует
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item; //  вещь которую бронируют
}
