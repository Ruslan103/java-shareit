package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto itemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .request(itemDto.getRequest())
                .build();
    }

    public static List<ItemDto> getItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::itemDto)
                .collect(Collectors.toList());
    }


    public static ItemDto itemDtoForResponse(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .lastBooking(item.getLastBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getLastBooking()) : null)
                .nextBooking(item.getNextBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getNextBooking()) : null)
                .build();
    }
}
