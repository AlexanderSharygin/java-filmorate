package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        User user = userDao.findById(userId).orElseThrow(() -> new NotExistException("Films are not exist in the DB"));
        Film film = filmDao.findById(filmId).orElseThrow(() -> new NotExistException("Films are not exist in the DB"));
        try {
            likeDao.add(film.getId(), user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already like film with id " + filmId);
        }
        return true;
    }

    public boolean removeLike(long filmId, long userId) {
        likeDao.find(filmId, userId)
                .orElseThrow(() -> new NotExistException("User with id " + userId + " is not like film with id " + filmId));
        likeDao.remove(filmId, userId);
        return true;
    }
}