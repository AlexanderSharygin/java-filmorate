package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Friend;

import java.util.Optional;

public interface FriendDao {

    Optional<Friend> find(Long userId, Long friendId);

    void add(Long userId, Long friendId);

    void confirm(Long userId, Long friendId);

    void remove(Long userId, Long friendId);
}
