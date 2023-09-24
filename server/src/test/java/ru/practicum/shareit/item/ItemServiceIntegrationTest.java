package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItServer.class, ItemServiceImpl.class, UserServiceImpl.class})
@TestPropertySource(properties = {"db.name=test"})
public class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Item item1;

    private ItemDto itemDto1;
    private ItemDto itemDto2;

    private UserDto userDto;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 5);
        User user1 = User.builder()
                .id(1)
                .name("UserName1")
                .email("test@test.com")
                .build();

        item1 = Item.builder()
                .id(1)
                .name("Item1Name")
                .description("Description for Item1")
                .available(true)
                .owner(user1)
                .build();

        Item item2 = Item.builder()
                .id(2)
                .name("Item2Name")
                .description("Description for Item2")
                .available(true)
                .owner(user1)
                .build();

        itemDto1 = ItemMapper.itemDto(item1);
        itemDto2 = ItemMapper.itemDto(item2);
        userDto = UserMapper.userDto(user1);
        userService.addUser(userDto);
    }

    @AfterTestMethod
    public void after() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addItemTest() {
        itemService.addItemDto(1, itemDto1);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item queryItem = query
                .setParameter("id", 1L)
                .getSingleResult();
        assertEquals(item1.getId(), queryItem.getId());
    }

    @Test
    void getItemsTest() {
        itemService.addItemDto(1, itemDto1);
        itemService.addItemDto(1, itemDto2);
        List<ItemDto> items = new ArrayList<>(itemService.getItems(1));
        assertEquals(2, items.size());
        assertEquals(item1.getName(), items.get(0).getName());
    }
}
