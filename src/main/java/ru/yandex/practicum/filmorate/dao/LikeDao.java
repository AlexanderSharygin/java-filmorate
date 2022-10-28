package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Like;

import java.util.Optional;

public interface LikeDao {
    Optional<Like> find(Long filmId, Long userId);

    void add(Long filmId, Long userId);

    void remove(Long filmId, Long userId);
}

