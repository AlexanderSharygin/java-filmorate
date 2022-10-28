package ru.yandex.practicum.filmorate.dao;

public interface GenreFilmDao {

    void addForFilm(Long filmId, Long genreId);

    void removeForFilm(Long filmId);
}