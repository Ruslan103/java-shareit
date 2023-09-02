package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(long userId, UserRepository userRepository, ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .requester(userRepository.getReferenceById(userId))
                .created(itemRequestDto.getCreated())
                .description(itemRequestDto.getDescription())
                .id(itemRequestDto.getId())
              //  .items(itemRequestDto.getItems())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRepository itemRepository, ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .requester(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .id(itemRequest.getId())
                .items(ItemMapper.getItemDtoList(itemRepository.findByRequestId(itemRequest.getId())))
                .build();
    }
    public static List<ItemRequestDto> getItemRequestDtoList(ItemRepository itemRepository,List<ItemRequest> itemRequestList) {
        return itemRequestList.stream()
                .map(itemRequest -> {
                    ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRepository, itemRequest);
                        List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
                        itemRequestDto.setItems(ItemMapper.getItemDtoList(items));
                    return itemRequestDto;
                })
                .collect(Collectors.toList());
    }
}
