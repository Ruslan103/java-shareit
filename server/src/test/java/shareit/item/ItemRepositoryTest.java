package shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    private User user;
    private User user2;
    private Item item;
    private Item item2;
    PageRequest pageRequest;

    ItemRequest itemRequest;
    ItemRequest itemRequest2;


    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 5);
        user = User.builder()
                .name("User1Name")
                .email("email@email.com")
                .build();

        user2 = User.builder()
                .name("User2Name")
                .email("emailUser2@email.com")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        itemRequest = ItemRequest.builder()
                .description("test")
                .requester(user)
                .build();

        itemRequest2 = ItemRequest.builder()
                .description("test2")
                .requester(user2)
                .build();

        itemRequestRepository.save(itemRequest);
        itemRequestRepository.save(itemRequest2);

        item = Item.builder()
                .name("ItemName")
                .description("text")
                .available(true)
                .request(itemRequest)
                .owner(user)
                .build();

        item2 = Item.builder()

                .name("Name")
                .description("Description")
                .available(true)
                .request(itemRequest2)
                .owner(user)
                .build();
        itemRepository.save(item);
        itemRepository.save(item2);
    }

    @Test
    void getItemsByDescription() {
        List<Item> items = new ArrayList<>(itemRepository.findByDescriptionContainingIgnoreCase("Description"));
        assertEquals(1, items.size());
        Assertions.assertEquals("Name", items.get(0).getName());
    }

    @Test
    void findByRequestId() {
        List<Item> items = new ArrayList<>(itemRepository.findByRequestId(1));
        assertNotNull(items);
        Assertions.assertEquals(item.getRequest().getId(), items.get(0).getId());
        assertEquals(1, items.size());
    }
}
