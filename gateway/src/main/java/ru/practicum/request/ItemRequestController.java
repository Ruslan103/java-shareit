package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated

public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "0", required = false) int from,
                                                 @RequestParam(defaultValue = "10", required = false) int size) {
        return itemRequestClient.getAllRequests(from, size, userId);

    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getIemRequestById(@PathVariable long requestId,
                                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getItemRequestById(requestId, userId);
    }
}
