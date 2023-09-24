package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;
    private PageRequest pageRequest;

    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;


    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 5, Sort.by("created").descending());
        User user = User.builder()
                .id(1)
                .name("NameUser1")
                .email("user1@email.com")
                .build();

        User user2 = User.builder()
                .id(2)
                .name("NameUser2")
                .email("user2@email.com")
                .build();

        User user3 = User.builder()
                .id(3)
                .name("NameUser3")
                .email("user3@email.com")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1)
                .description("Description")
                .created(LocalDateTime.now())
                .build();
        ItemRequest itemRequest2 = ItemRequest.builder()
                .id(2)
                .description("Description")
                .created(LocalDateTime.now())
                .build();

        Item item1 = Item.builder()
                .id(1)
                .name("NameItem1")
                .description("Description for item1")
                .available(true)
                .owner(user)
                .build();

        Item item2 = Item.builder()
                .id(2)
                .name("NameItem2")
                .description("Description for item2")
                .available(true)
                .owner(user2)
                .build();
        Item item3 = Item.builder()
                .id(3)
                .name("NameItem3")
                .description("Description for item3")
                .available(true)
                .owner(user3)
                .request(itemRequest)
                .build();


        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .description("Description")
                .requester(user.getId())
                .items(Collections.emptyList())
                .created(LocalDateTime.now())
                .build();
        itemRequestDto2 = ItemRequestDto.builder()
                .id(2)
                .description("Description")
                .requester(user.getId())
                .items(Collections.emptyList())
                .created(LocalDateTime.now())
                .build();

    }

    @Test
    void addRequest() throws Exception {
        when(itemRequestService.addItemRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class));

        verify(itemRequestService, times(1)).addItemRequest(1, itemRequestDto1);
    }

    @Test
    void getItemsRequest() throws Exception {
        when(itemRequestService.getItemRequests(anyLong())).thenReturn(List.of(itemRequestDto2));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto2))));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto2));
        mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(5))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto2))));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto2);

        mvc.perform(get("/requests/{requestId}", itemRequestDto2.getId())
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto2.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto2.getDescription()), String.class));
    }
}