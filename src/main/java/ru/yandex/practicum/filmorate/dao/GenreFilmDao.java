package ru.yandex.practicum.filmorate.dao;

public interface GenreFilmDao {

    void addGenreForFilm(Long filmId, Long genreId);

    void removeGenreForFilm(Long filmId);
}