package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> getFilmById(Long id);

    List<Film> getFilms();

    List<Film> getPopularFilms(Integer count);

    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Genre> getGenreById(Long id);

    List<Genre> getGenres();

    Optional<MPA> getMPAById(Long id);

    List<MPA> getMPAs();

    boolean addLIke(Long filmId, Long userId);

    boolean removeLike(Long filmId, Long userId);
}