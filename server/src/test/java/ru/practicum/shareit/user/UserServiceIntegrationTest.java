package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItServer.class, UserServiceImpl.class})
@TestPropertySource(properties = {"db.name=test"})
public class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void addUserTest() {
        UserDto user = UserDto.builder()
                .id(1)
                .name("NameUser")
                .email("test@email.com")
                .build();
        userService.addUser(user);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User queryUser = query
                .setParameter("id", user.getId())
                .getSingleResult();
        assertEquals(UserMapper.toUser(user), queryUser);
    }

    @Test
    void getUsersTest() {
        UserDto user1 = UserDto.builder()
                .id(1)
                .name("NameUser1")
                .email("test@email.com")
                .build();
        UserDto user2 = UserDto.builder()
                .id(2)
                .name("NameUser2")
                .email("test2@email.com")
                .build();
        userService.addUser(user1);
        userService.addUser(user2);
        List<User> allUsers = em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
        assertEquals(2, allUsers.size());
        assertEquals(UserMapper.toUser(user1), allUsers.get(0));
    }
}
