package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);

    UserDto getUserById(long userId);

    Collection<UserDto> getUsers();
}
