package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;

import java.util.List;

@RestController
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;

    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") long genreId) {
        return genreService.getGenreById(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return genreService.getGenres();
    }
}
