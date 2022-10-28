package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<User> {

    Optional<List<User>> findFriends(Long userId);

    Optional<List<User>> findCommonFriends(Long firstUserId, Long secondUserId);

    Optional<User> findNew();
}