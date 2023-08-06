package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.ValidationException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;


    private final UserRepository repository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (repository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Пользователь с таким Email уже существует"); //500
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Неверный Email"); // 400
        }
        repository.save(user);
        return UserMapper.userDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User oldUser = repository.getReferenceById(userId);
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        User anotherUser =repository.getUserByEmail(user.getEmail());
        if (repository.existsByEmail(user.getEmail())&&anotherUser.getId()!=userId) {
            throw new ValidationException("Пользователь с таким Email уже существует"); //500
        }
        user.setId(userId);
        repository.save(user);
        return UserMapper.userDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.userDto(repository.getReferenceById(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {
        return UserMapper.getItemDtoList(repository.findAll());
    }
}
