package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto itemDto(Item item) {
        if (item.getRequest() == null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .ownerId(item.getOwner().getId())
                    .lastBooking(item.getLastBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getLastBooking()) : null)
                    .nextBooking(item.getNextBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getNextBooking()) : null)
                    .comments(item.getComments())
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .ownerId(item.getOwner().getId())
                    .lastBooking(item.getLastBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getLastBooking()) : null)
                    .nextBooking(item.getNextBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getNextBooking()) : null)
                    .comments(item.getComments())
                    .requestId(item.getRequest().getId())
                    .build();
        }
    }

    public static Item toItem(UserRepository userRepository, long userId, ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userRepository.getReferenceById(userId))
                .request(null)
                .build();
    }

    public static List<ItemDto> getItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::itemDto)
                .collect(Collectors.toList());
    }


    public static ItemDto itemDtoForResponse(Item item) {
        if (item.getRequest() == null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .ownerId(item.getOwner().getId())
                    .lastBooking(item.getLastBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getLastBooking()) : null)
                    .nextBooking(item.getNextBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getNextBooking()) : null)
                    .comments(item.getComments())
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .ownerId(item.getOwner().getId())
                    .requestId(item.getRequest().getId())
                    .lastBooking(item.getLastBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getLastBooking()) : null)
                    .nextBooking(item.getNextBooking() != null ? BookingMapper.toLastAndNextBookingDto(item.getNextBooking()) : null)
                    .comments(item.getComments())
                    .build();
        }
    }
}
