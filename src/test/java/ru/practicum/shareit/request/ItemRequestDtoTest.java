package ru.practicum.shareit.request;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws IOException {
        LocalDateTime create = LocalDateTime.of(2023, 11, 11, 11, 0);
        User user = User.builder()
                .id(1L)
                .name("Name")
                .email("email@email.com")
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .requester(user)
                .description("Description")
                .id(1L)
                .created(create)
                .build();
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("ItemName")
                .description("Description")
                .available(true)
                .requestId(1L)
                .build();
        Object RequestMapper;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .items(Collections.emptyList())
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .build();
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(create.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
