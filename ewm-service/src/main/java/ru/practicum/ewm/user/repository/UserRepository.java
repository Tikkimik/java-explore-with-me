package ru.practicum.ewm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user FROM User user WHERE user.id IN :userIds")
    List<User> findUsersByIds(List<Long> userIds);

}
