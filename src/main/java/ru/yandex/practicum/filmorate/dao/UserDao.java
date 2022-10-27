package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUserById(Long id);

    List<User> findUsers();

    List<User> findFriendsForUser(Long userId);

    List<User> findCommonFriends(Long firstUserId, Long secondUserId);

    void addUser(User user);

    void updateUser(User user);

    Optional<User> findNewUser();
}