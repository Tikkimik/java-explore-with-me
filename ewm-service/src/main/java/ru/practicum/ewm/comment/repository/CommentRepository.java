package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> getAllByUserId(long userId, PageRequest pageRequest);

    Page<Comment> getAllByEventId(long eventId, PageRequest pageRequest);

    Page<Comment> findAllByMessageContainsIgnoreCase(String text, PageRequest pageRequest);

}
