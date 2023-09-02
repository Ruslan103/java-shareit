package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getItemRequests(long userId);

    List<ItemRequestDto> getAllRequests(long userId, int from, int size);

    ItemRequestDto getIemRequestById(long requestId, long userId);
}
