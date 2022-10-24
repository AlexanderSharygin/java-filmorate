package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DAOs.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class FilmController {

    private final FilmDao filmDao;

    @Autowired
    public FilmController(FilmDao filmDao) {
        this.filmDao = filmDao;

    }

    @GetMapping("/films/{id}")
    public Optional<Film> getFilmById(@PathVariable("id") long filmId) {
        return filmDao.getFilmById(filmId);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmDao.getFilms();
    }

    @PostMapping(value = "/films")
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        return filmDao.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        return filmDao.updateFilm(film);
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getGenreById(@PathVariable("id") long genreId) {
        return filmDao.getGenreById(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmDao.getGenres();
    }

    @GetMapping("/mpa/{id}")
    public Optional<MPA> getMPAById(@PathVariable("id") long mpaId) {
        return filmDao.getMPAById(mpaId);
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAs() {
        return filmDao.getMPAs();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public boolean addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
       return filmDao.setLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmDao.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {
            throw new BadRequestException("count");
        }
        return filmDao.getPopularFilms(count);
    }
}