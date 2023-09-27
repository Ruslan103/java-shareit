package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.exception.LineNotNullException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CommentDto addComment(long userId, long itemId, Comment comment) {
        if (comment.getText().isEmpty()) {
            throw new LineNotNullException("Comment cannot be empty");
        }
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(itemId);
        List<Status> statuses = List.of(Status.APPROVED);
        Booking booking = bookingRepository.findTopByBookerAndItemOrderByStart(booker, item);
        if (bookingRepository.isBookerAndItemExist(booker, item, statuses) && booking.getEnd().isBefore(LocalDateTime.now())) {
            return CommentMapper.toCommentDto(
                    commentRepository.save(Comment.builder()
                            .text(comment.getText())
                            .itemId(itemId)
                            .userId(userId)
                            .name(item.getName())
                            .authorName(booker.getName())
                            .created(LocalDateTime.now())
                            .build())
            );
        } else {
            throw new ItemUnavailableException("This user has not used the item");
        }
    }
}
