package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMPAById(long id) {
        try {
            return mpaDao.findMpaById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("MPA with id " + id + " not exists in the DB");
        }
    }

    public List<Mpa> getAllMpa() {
        try {
            return mpaDao.findAllMpa();
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }
}
