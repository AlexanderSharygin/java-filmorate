package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findUserById(Long id);

    Optional<User> add(User user);
}
