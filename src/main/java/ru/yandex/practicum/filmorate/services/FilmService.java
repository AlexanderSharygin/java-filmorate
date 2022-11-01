package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.GenreFilmDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final GenreDao genreDao;

    private final GenreFilmDao genreFilmDao;

    @Value("${filmorate.minDate}")
    private String minDate;

    @Autowired
    public FilmService(FilmDao filmDao, GenreDao genreDao, GenreFilmDao genreFilmDao) {
        this.filmDao = filmDao;
        this.genreDao = genreDao;
        this.genreFilmDao = genreFilmDao;
    }

    public Film getFilmById(long id) {
        Film film = filmDao.findById(id)
                .orElseThrow(() -> new NotExistException("Film with id " + id + " not exists in the DB"));
        film.setGenres(genreDao.findByFilmId(film.getId()));
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDao.findAll();
        films.forEach(k -> k.setGenres(genreDao.findByFilmId(k.getId())));
        return films;
    }

    public Film addFilm(Film film) {
        validate(film);
        Film createdFilm;
        try {
            filmDao.add(film);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Mpa rate with specified id not exists in the DB");
        }
        createdFilm = filmDao.findNew().orElseThrow(
                () -> new AlreadyExistException("Film already exists in the DB"));
        addGenresForFilm(film, createdFilm.getId());
        createdFilm.setGenres(genreDao
                .findByFilmId(createdFilm.getId()));
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        validate(film);
        try {
            genreFilmDao.removeForFilm(film.getId());
            filmDao.update(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film.getId() + " not exists in the DB");
        }
        Film updatedFilm = getFilmById(film.getId());
        addGenresForFilm(film, updatedFilm.getId());
        updatedFilm.setGenres(genreDao
                .findByFilmId(film.getId()));
        return updatedFilm;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmDao.findPopulars(count);
        films.forEach(k -> k.setGenres(genreDao
                .findByFilmId(k.getId())));
        return films;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse(minDate))) {
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("MPA rate should be specified for the film");
        }
    }

    private void addGenresForFilm(Film oldFilm, Long newFilmId) {
        Set<Long> genreIds = new HashSet<>((oldFilm.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList())));
        for (var genre : genreIds) {
            try {
                genreFilmDao.addForFilm(newFilmId, genre);
            } catch (DuplicateKeyException e) {
                throw new AlreadyExistException("Film with genre already exists in the DB");
            }
        }
    }
}