package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Integer countUsersByName(String name);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findAllUsersByIds(List<Long> ids, Pageable pageable);

    @Query("SELECT u FROM User u")
    List<User> findAllUsers(Pageable pageable);
}
