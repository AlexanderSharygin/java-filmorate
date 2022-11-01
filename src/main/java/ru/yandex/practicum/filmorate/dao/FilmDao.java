package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao extends Dao<Film> {
    Optional<Film> findNew();

    List<Film> findPopulars(Integer count);
}