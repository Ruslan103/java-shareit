package shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.exception.RequestParameterException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private PageRequest pageRequest;
    private User user1;
    private User user2;
    private User user3;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;
    private Item item1;
    private Item item2;
    private Item item3;

    private ItemRequestDto itemRequestDto;


    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 5);
        user1 = User.builder()
                .id(1)
                .name("Harrison")
                .email("Ford@test.com")
                .build();

        user2 = User.builder()
                .id(2)
                .name("Tom")
                .email("cruise@test.com")
                .build();

        user3 = User.builder()
                .id(3)
                .name("Jamie")
                .email("fox@test.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .requester(user1)
                .description("Nintendo")
                .created(LocalDateTime.now())
                .build();
        itemRequest2 = ItemRequest.builder()
                .id(2)
                .requester(user2)
                .description("Sega")
                .created(LocalDateTime.now())
                .build();

        item1 = Item.builder()
                .id(1)
                .name("XBOX")
                .description("console from Microsoft")
                .available(true)
                .owner(user1)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("Play Station")
                .description("Gaming console")
                .available(true)
                .owner(user2)
                .build();
        item3 = Item.builder()
                .id(3)
                .name("Nintendo")
                .description("fyi")
                .available(true)
                .owner(user3)
                .request(itemRequest)
                .build();
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRepository, itemRequest);
    }

    @Test
    void addItemRequest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequestDto itemRequestDtoTest = itemRequestService.addItemRequest(1, itemRequestDto);
        Assertions.assertEquals(itemRequestDtoTest.getId(), itemRequestDto.getId());
    }

    @Test
    void addItemRequestWithNotExistUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> itemRequestService.addItemRequest(100, itemRequestDto));
    }

    @Test
    void addItemRequestWithNullDescription() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        itemRequestDto.setDescription(null);
        assertThrows(LineNotNullException.class, () -> itemRequestService.addItemRequest(1, itemRequestDto));
    }

    @Test
    void getItemRequests() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findRequestByRequesterId(anyLong())).thenReturn(List.of(itemRequest, itemRequest2));
        List<ItemRequest> itemRequestsDto = List.of(itemRequest, itemRequest2);
        List<ItemRequestDto> itemRequestsDtoTest = itemRequestService.getItemRequests(1);
        assertEquals(itemRequestsDto.size(), itemRequestsDtoTest.size());
        Assertions.assertEquals(itemRequestsDto.get(0).getId(), itemRequestsDtoTest.get(0).getId());
    }

    @Test
    void getItemRequestsWithNotExistUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> itemRequestService.getItemRequests(100));
    }

    @Test
    void getAllRequest() {
        when(itemRequestRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(itemRequest), pageRequest, 1));
        List<ItemRequestDto> itemRequestList = itemRequestService.getAllRequests(2, 1, 5);
        assertEquals(itemRequestList.size(), 1);
    }

    @Test
    void getAllRequestWithWrongFrom() {
        assertThrows(RequestParameterException.class, () -> itemRequestService.getAllRequests(1, -1, 5));
    }

    @Test
    void getIemRequestById() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(itemRequest);
        ItemRequestDto itemRequestDtoTest = itemRequestService.getItemRequestById(1, 1);
        Assertions.assertEquals(itemRequestDtoTest.getId(), itemRequest.getId());
    }

    @Test
    void getIemRequestByIdWithNotExistUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ValidationException.class, () -> itemRequestService.getItemRequestById(100, 100));
    }

    @Test
    void getIemRequestByIdWithNotExistURequest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> itemRequestService.getItemRequestById(100, 100));
    }
}
