package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.FilmGenre;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmsGenresDao  {

    void addGenreForFilm(Long filmId, Long genreId);

    void removeGenreForFilm(Long filmId);
}
