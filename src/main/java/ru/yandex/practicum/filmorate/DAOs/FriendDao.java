package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.Friend;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface FriendDao {
    List<User> getCommonFriends(Long firstUserId, Long secondUserId);

    void addFriend(Long userId, Long friendId);

    void confirmFriend(Long userId, Long friendId);

    Optional<Friend> getFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
