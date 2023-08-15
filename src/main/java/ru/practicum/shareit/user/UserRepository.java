package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    User getUserByEmail(String email);
}
