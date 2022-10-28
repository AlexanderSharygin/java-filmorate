package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(long id) {
        return genreDao.findById(id)
                .orElseThrow(() -> new NotExistException("Genre with id " + id + " not exists in the DB"));
    }

    public List<Genre> getGenres() {
        return genreDao.findAll().orElseThrow(() -> new BadRequestException("Something went wrong."));
    }
}