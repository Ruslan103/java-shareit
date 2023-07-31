package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.userDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        return UserMapper.userDto(userStorage.updateUser(userId, user));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.userDto(userStorage.getUserById(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {
        return UserMapper.getItemDtoList(userStorage.getUsers());
    }
}
