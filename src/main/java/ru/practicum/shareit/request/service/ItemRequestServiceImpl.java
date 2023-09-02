package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.exception.RequestParameterException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    public ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundByIdException("User by id not found"); // 404
        }
        if (itemRequestDto.getDescription() == null) {
            throw new LineNotNullException("Description cannot be empty");
        }
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(userId, userRepository, itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRepository, itemRequestRepository.save(itemRequest));
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getItemRequests(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundByIdException("User by id not found"); // 404
        }
        List<ItemRequest> list = itemRequestRepository.findRequestByRequesterId(userId);
        return ItemRequestMapper.getItemRequestDtoList(itemRepository, list);
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllRequests(long userId, int from, int size) {
        if (from < 0) {
            throw new RequestParameterException("Invalid request parameter");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<ItemRequest> list = itemRequestRepository.findAll(pageable).toList().stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() != userId)
                .collect(Collectors.toList());
        return ItemRequestMapper.getItemRequestDtoList(itemRepository, list);
    }

    @Transactional(readOnly = true)
    public ItemRequestDto getIemRequestById(long requestId, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("User by id not found"); // 404
        }
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundByIdException("Request by id not found"); // 404
        }
        ItemRequest itemRequest = itemRequestRepository.getReferenceById(requestId);
        return ItemRequestMapper.toItemRequestDto(itemRepository, itemRequest);
    }
}
