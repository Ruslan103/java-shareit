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
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private Comment comment;
    private CommentDto commentDto;
    private ItemRequest itemRequest;
    private Booking booking;
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

        booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void addCommentTest() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item1);
        when(bookingRepository.findTopByBookerAndItemOrderByStart(any(User.class), any(Item.class))).thenReturn(booking);
        when(bookingRepository.isBookerAndItemExist(any(User.class), any(Item.class), anyList())).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentDto commentDto1 = commentService.addComment(1, 1, comment);
        Assertions.assertEquals(commentDto1.getId(), 1);
        Assertions.assertEquals(commentDto1.getText(), "commentText");
    }

    @Test
    void addCommentWithNotBookerExist() {
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item1);
        when(bookingRepository.findTopByBookerAndItemOrderByStart(any(User.class), any(Item.class))).thenReturn(booking);
        when(bookingRepository.isBookerAndItemExist(any(User.class), any(Item.class), anyList())).thenReturn(false);
        assertThrows(ItemUnavailableException.class, () -> commentService.addComment(100, 1, comment));
    }

    @Test
    void addCommentWithEmptyText() {
        comment.setText("");
        assertThrows(LineNotNullException.class, () -> commentService.addComment(100, 1, comment));
    }
}
