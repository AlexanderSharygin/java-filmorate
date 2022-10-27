package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> findFilmById(Long id);


    Optional<Film> findNewestFilm();

    List<Film> findFilms();

    List<Film> findPopularFilms(Integer count);

    void addFilm(Film film);

    void updateFilm(Film film);
}