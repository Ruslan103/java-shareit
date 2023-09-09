package ru.practicum.shareit.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user1;

    private User user2;

    private UserDto userDto1;

    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .id(1)
                .name("NameUser1")
                .email("emailUser1@test.com")
                .build();
        userDto1 = UserMapper.userDto(user1);
        user2 = User.builder()
                .id(2)
                .name("NameUser2")
                .email("emailUser2@test.com")
                .build();
        userDto2 = UserMapper.userDto(user2);
    }

    @Test
    void addUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user1);
        UserDto userDtoTest = userService.addUser(userDto1);
        assertEquals(userDtoTest.getId(), userDto1.getId());
        assertEquals(userDtoTest.getName(), userDto1.getName());
        assertEquals(userDtoTest.getEmail(), userDto1.getEmail());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void addUserWithWrongEmail() {
        userDto1.setEmail(null);
        assertThrows(InvalidEmailException.class, () -> userService.addUser(userDto1));
    }

    @Test
    void updateUserTest() {
        user1.setName("NewName");
        when(userRepository.save(any(User.class))).thenReturn(user1);
        UserDto userDtoTest = userService.updateUser(1, userDto1);
        assertEquals(userDtoTest.getName(), "NewName");
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void updateUserWithWrongEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userRepository.getUserByEmail(anyString())).thenReturn(user1);
        userDto1.setEmail(user1.getEmail());
        userDto1.setId(2);
        assertThrows(ValidationException.class, () -> userService.updateUser(2, userDto1));
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user1);
        UserDto userTest = userService.getUserById(1);
        assertEquals(user1.getId(), userTest.getId());
        assertEquals(user1.getName(), userTest.getName());
        assertEquals(user1.getEmail(), userTest.getEmail());
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        Collection<UserDto> users = List.of(userDto1, userDto2);
        Collection<UserDto> usersTest = userService.getUsers();
        assertEquals(usersTest, users);
    }
}
