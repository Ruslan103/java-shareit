package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Invalid Email"); // 400
        }
        return UserMapper.userDto(repository.save(user));
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
        User anotherUser = repository.getUserByEmail(user.getEmail());
        if (repository.existsByEmail(user.getEmail()) && anotherUser.getId() != userId) {
            throw new ValidationException("User with this Email already exists"); //500
        }
        user.setId(userId);
        return UserMapper.userDto(repository.save(user));
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        if (repository.existsById(userId)) {
            return UserMapper.userDto(repository.getReferenceById(userId));
        } else {
            throw new NotFoundByIdException("User by id not found");
        }
    }

    @Override
    public Collection<UserDto> getUsers() {
        return UserMapper.getItemDtoList(repository.findAll());
    }
}
