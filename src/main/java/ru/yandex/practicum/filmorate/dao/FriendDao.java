package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Friend;

import java.util.Optional;

public interface FriendDao {

    void addFriend(Long userId, Long friendId);

    void confirmFriend(Long userId, Long friendId);

    Optional<Friend> findFriendCombination(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
