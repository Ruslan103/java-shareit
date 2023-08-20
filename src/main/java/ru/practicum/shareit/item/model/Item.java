package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available")
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    @Column(name = "owner_id")
    private Long owner; // id владелеца вещи
    @Column(name = "request_id")
    private Long request; //  id если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;

//    public Item(long id, String name, String description, Boolean available, Long owner, Long request) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.available = available;
//        this.owner = owner;
//        this.request = request;
//    }

    public Item(long id, String name, String description, Boolean available, Long owner, Long request, Booking lastBooking, Booking nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }

    public Item() {
    }
}
