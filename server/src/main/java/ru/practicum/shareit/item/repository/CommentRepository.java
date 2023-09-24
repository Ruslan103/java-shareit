package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByItemId(long itemId);

    @Query("SELECT COUNT(c) > 0 " +
            "FROM Comment c " +
            "WHERE c.itemId = ?1")
    boolean existsByItemId(Long itemId);
}
