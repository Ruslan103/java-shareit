package shareit.item;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;


    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private Comment comment;
    private CommentDto commentDto;
    private ItemRequest itemRequest;
    private Booking booking1;
    private Booking booking2;
    private PageRequest pageRequest;

    @BeforeEach
    void beforeEach() {
        pageRequest = PageRequest.of(0, 5);
        user1 = User.builder()
                .id(1)
                .name("NameUser1")
                .email("emailUser1@test.com")
                .build();

        user2 = User.builder()
                .id(2)
                .name("NameUser2")
                .email("emailUser2@test.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("ItemRequest")
                .created(LocalDateTime.now())
                .build();

        item1 = Item.builder()
                .id(1)
                .name("item1name")
                .description("item1description")
                .available(true)
                .owner(user1)
                .request(itemRequest)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("item2name")
                .description("item2description")
                .available(true)
                .owner(user2)
                .build();


        itemDto1 = ItemMapper.itemDto(item1);
        itemDto2 = ItemMapper.itemDto(item2);

        comment = Comment.builder()
                .id(1)
                .itemId(item1.getId())
                .userId(user1.getId())
                .authorName(user1.getName())
                .created(LocalDateTime.now())
                .text("commentText")
                .build();

        commentDto = CommentMapper.toCommentDto(comment);

        booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();
        booking2 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void addItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        ItemDto itemDtoTest = itemService.addItemDto(1, itemDto1);
        Assertions.assertEquals(itemDtoTest.getId(), itemDto1.getId());
        Assertions.assertEquals(itemDtoTest.getName(), itemDto1.getName());
        Assertions.assertEquals(itemDtoTest.getRequestId(), itemDto1.getRequestId());
        Assertions.assertEquals(itemDtoTest.getDescription(), itemDto1.getDescription());
        Assertions.assertEquals(itemDtoTest.getAvailable(), itemDto1.getAvailable());
        Assertions.assertEquals(itemDtoTest.getComments(), itemDto1.getComments());
    }

    @Test
    void addItemWithEmptyName() {
        itemDto1.setName("");
        assertThrows(LineNotNullException.class, () -> itemService.addItemDto(1, itemDto1));
    }

    @Test
    void addItemWithNullDescription() {
        itemDto1.setDescription(null);
        assertThrows(LineNotNullException.class, () -> itemService.addItemDto(1, itemDto1));
    }

    @Test
    void addItemWithNullAvailable() {
        itemDto1.setAvailable(null);
        assertThrows(LineNotNullException.class, () -> itemService.addItemDto(1, itemDto1));
    }

    @Test
    void addItemWithNotExistUser() {
        assertThrows(NotFoundByIdException.class, () -> itemService.addItemDto(100, itemDto1));
    }

    @Test
    void updateItemTest() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item1);
        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        item1.setAvailable(true);
        item1.setName("NewItem1Name");
        ItemDto updateItem = itemService.updateItem(1, 1, itemDto1);
        Assertions.assertEquals(item1.getName(), updateItem.getName());
        Assertions.assertEquals(item1.getAvailable(), updateItem.getAvailable());
    }

    @Test
    void updateItemWithNotExistItem() {
        when(itemRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> itemService.updateItem(100, 100, itemDto1));
    }

    @Test
    void getItemByIdTest() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item1);
        when(commentRepository.findCommentsByItemId(anyLong())).thenReturn(List.of(comment));
        ItemDto itemTest = itemService.getItemById(1, 1);
        Assertions.assertEquals(itemTest.getId(), 1);
    }

    @Test
    void getItemById() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item1);
        when(commentRepository.findCommentsByItemId(anyLong())).thenReturn(List.of(comment));
        when(bookingRepository.findFirstByItemAndStatusIsInAndStartBeforeOrderByStartDesc(any(Item.class), anyList(), any(LocalDateTime.class)))
                .thenReturn(booking1);
        when(bookingRepository.findFirstByItemAndStatusIsInAndEndAfterOrderByStartAsc(any(Item.class), anyList(), any(LocalDateTime.class)))
                .thenReturn(booking2);
        ItemDto itemTest = itemService.getItemById(1, 1);
        Assertions.assertEquals(itemTest.getId(), 1);
    }

    @Test
    void getItemWithNotExistId() {
        when(itemRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> itemService.getItemById(100, 100));
    }

    @Test
    void getItemsListTest() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        when(itemRepository.getItemByOwner(any(User.class))).thenReturn(List.of(item1, item2));
        List<ItemDto> itemListTest = List.of(itemDto1, itemDto2);
        List<ItemDto> itemList = itemService.getItems(1L);
        Assertions.assertEquals(itemListTest.get(1).getId(), itemList.get(1).getId());
        assertEquals(itemListTest.size(), itemList.size());
    }

    @Test
    void getItemsByDescriptionTest() {
        when(itemRepository.findByDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of(item1, item2));
        List<ItemDto> itemDtoListTest = List.of(itemDto1, itemDto2);
        List<ItemDto> itemDtoList = itemService.getItemsByDescription("text");
        assertEquals(itemDtoListTest, itemDtoList);
    }

    @Test
    void getItemsByEmptyDescriptionTest() {
        List<ItemDto> itemDtoList = itemService.getItemsByDescription("");
        assertEquals(itemDtoList, new ArrayList<>());
    }
}
