package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAOs.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    @Value("${filmorate.minDate}")
    private String minDate;

    @Autowired
    public FilmService(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    public List<Film> getPopularFilms(int count) {
        return filmDao.getPopularFilms(count);
    }

    public Optional<Film> getFilmById(long id) {
        return filmDao.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmDao.getFilms();
    }

    public Optional<Film> addFilm(Film film) {
        validate(film);

        return filmDao.addFilm(film);
    }

    public boolean addLike(long filmId, long userId) {
        return filmDao.addLIke(filmId, userId);
    }

    public Optional<Film> updateFilm(Film film) {
        validate(film);

        return filmDao.updateFilm(film);
    }

    public boolean removeLike(long filmId, long userId) {
        return filmDao.removeLike(filmId, userId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse(minDate))) {
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("MPA rate should be specified for the film");
        }
    }

    public Optional<Genre> getGenreById(long id) {
        return filmDao.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return filmDao.getGenres();
    }

    public Optional<MPA> getMPAById(long id) {
        return filmDao.getMPAById(id);
    }

    public List<MPA> getMPAs() {
        return filmDao.getMPAs();
    }
}