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
        Film film;
        try {
            film = filmDao.findFilmById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + id + " not exists in the DB");
        }
        try {
            film.setGenres(genreDao.findGenresByFilmId(film.getId()));
        } catch (RuntimeException e) {
            film.setGenres(new ArrayList<>());
        }
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films;
        try {
            films = filmDao.findFilms();
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
        for (var film : films) {
            try {
                film.setGenres(genreDao.findGenresByFilmId(film.getId()));
            } catch (RuntimeException exception) {
                film.setGenres(new ArrayList<>());
            }
        }
        return films;
    }

    public Film addFilm(Film film) {
        validate(film);
        Film createdFilm;
        try {
            filmDao.addFilm(film);
            createdFilm = filmDao.findNewestFilm().get();
        } catch (
                DuplicateKeyException e) {
            throw new AlreadyExistException("Film already exists in the DB");
        } catch (
                DataIntegrityViolationException e) {
            throw new AlreadyExistException("Mpa rate with specified id not exists in the DB");
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
        try {
            addGenresForFilm(film, createdFilm.getId());
        } catch (RuntimeException e) {
            film.setGenres(new ArrayList<>());
        }
        try {
            createdFilm.setGenres(genreDao.findGenresByFilmId(createdFilm.getId()));
        } catch (RuntimeException e) {
            createdFilm.setGenres(new ArrayList<>());
        }
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        validate(film);
        try {
            genreFilmDao.removeGenreForFilm(film.getId());
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
        try {
            filmDao.updateFilm(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film.getId() + " not exists in the DB");
        }

        Film updatedFilm;
        try {
            updatedFilm = getFilmById(film.getId());
        } catch (
                EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film.getId() + " not exists in the DB");
        }
        addGenresForFilm(film, updatedFilm.getId());
        try {
            updatedFilm.setGenres(genreDao.findGenresByFilmId(film.getId()));
        } catch (RuntimeException e) {
            updatedFilm.setGenres(new ArrayList<>());
        }
        return updatedFilm;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films;
        try {
            films = filmDao.findPopularFilms(count);
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
        for (var film : films) {
            film.setGenres(genreDao.findGenresByFilmId(film.getId()));
        }
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
        Set<Long> genreIds = new HashSet<>((oldFilm.getGenres().stream().map(Genre::getId).collect(Collectors.toList())));
        for (var genre : genreIds) {
            try {
                genreFilmDao.addGenreForFilm(newFilmId, genre);
            } catch (DuplicateKeyException e) {
                throw new AlreadyExistException("Film with genre already exists in the DB");
            } catch (RuntimeException e) {
                throw new BadRequestException("Something went wrong.");
            }
        }
    }
}