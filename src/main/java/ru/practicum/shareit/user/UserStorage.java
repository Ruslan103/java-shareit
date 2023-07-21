package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);

    User updateUser(long userId,User user);

    void deleteUser(long userId);

    User getUserById(long userId);

    Collection<User> getUsers();
}
