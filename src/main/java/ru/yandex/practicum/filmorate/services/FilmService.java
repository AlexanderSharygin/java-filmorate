package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAOs.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;

    private final UserDao userDao;

    private final LikeDao likeDao;

    private final FilmsGenresDao filmsGenresDao;

    @Value("${filmorate.minDate}")
    private String minDate;

    @Autowired
    public FilmService(FilmDao filmDao, GenreDao genreDao, MpaDao mpaDao, UserDao userDao, LikeDao likeDao, FilmsGenresDao filmsGenresDao) {
        this.filmDao = filmDao;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.userDao = userDao;
        this.likeDao = likeDao;
        this.filmsGenresDao = filmsGenresDao;
    }

    public Film getFilmById(long id) {
        Film film = filmDao.getFilmById(id).get();
        film.setGenres(genreDao.findGenresByFilmId(film.getId()));
        return film;
    }


    public List<Film> getFilms() {
        List<Film> films = filmDao.getFilms();
        for (var film : films) {
            film.setGenres(genreDao.findGenresByFilmId(film.getId()));
        }
        return films;
    }

    public Film addFilm(Film film) {
        validate(film);
        filmDao.addFilm(film);
        Film createdFilm = filmDao.getNewestFilm().get();
        addGenresForFilm(film, createdFilm.getId());
        createdFilm.setGenres(genreDao.findGenresByFilmId(createdFilm.getId()));
        return createdFilm;
    }

    private void addGenresForFilm(Film oldFilm, Long newFilmId)
    {
        Set<Long> genreIds = new HashSet<>((oldFilm.getGenres().stream().map(Genre::getId).collect(Collectors.toList())));
        for (var genre : genreIds) {
            filmsGenresDao.addGenreForFilm(newFilmId, genre);
        }
    }

    public Film updateFilm(Film film) {
        validate(film);
        filmsGenresDao.removeGenreForFilm(film.getId());
        filmDao.updateFilm(film);
        Film updatedFilm = getFilmById(film.getId());
        addGenresForFilm(film, updatedFilm.getId());
        updatedFilm.setGenres(genreDao.findGenresByFilmId(film.getId()));
        return updatedFilm;
    }


    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmDao.getPopularFilms(count);
        for (var film : films) {
            film.setGenres(genreDao.findGenresByFilmId(film.getId()));
        }
        return films;
    }


    public boolean addLike(long filmId, long userId) {
        User user = userDao.findUserById(userId).get();
        Film film = filmDao.getFilmById(filmId).get();
        likeDao.addLIke(film.getId(), user.getId());
        return true;
    }


    public boolean removeLike(long filmId, long userId) {
        Like like = likeDao.getLIke(filmId, userId).get();
        likeDao.removeLike(like.getFilmId(), like.getUserId());
        return true;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse(minDate))) {
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("MPA rate should be specified for the film");
        }
    }

    public Genre getGenreById(long id) {
        return genreDao.getGenreById(id).orElse(null);
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public MPA getMPAById(long id) {
        return mpaDao.getMpaById(id);
    }

    public List<MPA> getMPAs() {
        return mpaDao.getMpas();
    }
}