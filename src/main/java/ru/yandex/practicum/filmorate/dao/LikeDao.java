package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Like;

import java.util.Optional;

public interface LikeDao {
    Optional<Like> findLIke(Long filmId, Long userId);

    void addLIke(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}

