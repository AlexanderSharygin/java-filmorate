package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUserById(Long id);

    List<User> getUsers();

    List<User> getFriendsForUser(Long userId);

    List<User> getCommonFriends(Long firstUserId, Long secondUserId);

    void addUser(User user);

    void updateUser(User user);

    Optional<User> findNewUser();
}