package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> getFilmById(Long id);


    Optional<Film> getNewestFilm();

    List<Film> getFilms();

    List<Film> getPopularFilms(Integer count);

    void addFilm(Film film);

    void updateFilm(Film film);
}