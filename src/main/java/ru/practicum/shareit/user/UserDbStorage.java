package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
//import ru.practicum.shareit.exception.InvalidEmailException;


import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User addUser(User user) {
        if (users.containsValue(user)) {
            throw new ValidationException("Пользователь с таким Email уже существует"); //500
        }
//        if (user.getEmail() == null || !user.getEmail().contains("@")) {
//            throw new InvalidEmailException("Неверный Email"); // 400
//        }
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        User oldUser = users.get(userId);
        for (User newUser : users.values()) {
            if (Objects.equals(newUser.getEmail(), user.getEmail()) && newUser.getId() != userId) {
                throw new ValidationException("Пользователь с таким Email уже существует"); //500
            }
        }


        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }
}
