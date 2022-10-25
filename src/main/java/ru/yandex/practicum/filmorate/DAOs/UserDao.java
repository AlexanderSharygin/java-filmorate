package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(Long id);

    List<User> getUsers();

    List<User> getFriendsForUser(Long userId);

    List<User> getCommonFriends(Long firstUserId, Long secondUserId);

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    void addFriend(Long userId, Long friendId);

    void confirmFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
