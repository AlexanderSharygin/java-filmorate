package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getGenreById(Long id);

    List<Genre> findGenresByFilmId(Long id);

    List<Genre> getGenres();
}
