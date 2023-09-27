package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.exception.NotFoundByIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Invalid Email"); // 400
        }
        return UserMapper.userDto(repository.save(user));
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long userId) {
        if (repository.existsById(userId)) {
            return UserMapper.userDto(repository.getReferenceById(userId));
        } else {
            throw new NotFoundByIdException("User by id not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getUsers() {
        return UserMapper.getItemDtoList(repository.findAll());
    }
}
