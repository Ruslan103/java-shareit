package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItApp.class, ItemServiceImpl.class, UserServiceImpl.class, ItemServiceImpl.class})
public class ItemRequestIntegrationTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final ItemRequestServiceImpl itemRequestService;

    private User owner;
    private User requester;

    private Item item;

    private ItemRequest request;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1)
                .name("Name")
                .email("email1@email.com")
                .build();

        requester = User.builder()
                .id(2)
                .name("Name")
                .email("email@email.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("ItemName")
                .description("Description")
                .available(true)
                .owner(owner)
                .build();

        request = ItemRequest.builder()
                .description("Description")
                .created(LocalDateTime.of(2023, 12, 12, 12, 0))
                .requester(requester)
                .id(1)
                .build();

        item = Item.builder()
                .id(1)
                .name("ItemName")
                .description("Description")
                .available(true)
                .request(request)
                .owner(owner)
                .build();
    }

    @Test
    void addBooking() {
        userService.addUser(UserMapper.userDto(owner));
        userService.addUser(UserMapper.userDto(requester));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description(request.getDescription())
                .requester(request.getRequester().getId())
                .items(Collections.emptyList())
                .created(request.getCreated())
                .build();
        itemRequestService.addItemRequest(2, itemRequestDto);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.id = :id", ItemRequest.class);
        ItemRequest queryRequest = query
                .setParameter("id", 1L)
                .getSingleResult();
        assertNotNull(queryRequest);
        assertEquals("Description", queryRequest.getDescription());
        assertEquals(request.getRequester().getId(), queryRequest.getRequester().getId());
    }

    @Test
    void getIemRequestById() {
        itemService.addItemDto(1, ItemMapper.itemDto(item));
        ItemRequestDto result = itemRequestService.getIemRequestById(1, 2);
        assertEquals("Description", result.getDescription());
        assertEquals(ItemMapper.itemDtoForResponse(item), result.getItems().get(0));
    }
}
