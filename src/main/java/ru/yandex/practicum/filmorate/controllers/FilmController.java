package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") long filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getFilms();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") long genreId) {
        return filmService.getGenreById(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPAById(@PathVariable("id") long mpaId) {
        return filmService.getMPAById(mpaId);
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAs() {
        return filmService.getMPAs();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public boolean addLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {

            throw new BadRequestException("count");
        }

        return filmService.getPopularFilms(count);
    }
}