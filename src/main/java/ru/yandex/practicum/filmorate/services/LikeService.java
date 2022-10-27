package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;


@Service
@Slf4j
public class LikeService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final LikeDao likeDao;

    @Autowired
    public LikeService(FilmDao filmDao, UserDao userDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.likeDao = likeDao;
    }

    public boolean addLike(long filmId, long userId) {
        User user;
        Film film;
        try {
            user = userDao.findUserById(userId).get();
            film = filmDao.findFilmById(filmId).get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Films are not exist in the DB");
        }
        try {
            likeDao.addLIke(film.getId(), user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already like film with id " + filmId);
        }
        return true;
    }

    public boolean removeLike(long filmId, long userId) {
        try {
            likeDao.findLIke(filmId, userId).get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + userId + " is not like film with id " + filmId);
        }
        try {
            likeDao.removeLike(filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + userId + " is not like film with id " + filmId);
        }

        return true;
    }
}