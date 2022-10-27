package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.LikeService;

import javax.validation.Valid;
import java.util.List;

@RestController

public class FilmController {
    private final FilmService filmService;

    private final LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
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

    @PutMapping("/films/{id}/like/{userId}")
    public boolean addLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return likeService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return likeService.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {

            throw new BadRequestException("count");
        }

        return filmService.getPopularFilms(count);
    }
}