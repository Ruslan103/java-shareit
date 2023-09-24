package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto userDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static Collection<UserDto> getItemDtoList(Collection<User> users) {
        return users.stream()
                .map(UserMapper::userDto)
                .collect(Collectors.toList());
    }
}
