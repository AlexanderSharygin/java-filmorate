package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findById(Long id);

    Optional<List<Genre>> findByFilmId(Long id);

    Optional<List<Genre>> findAll();
}