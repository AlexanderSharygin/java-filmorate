package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
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
        return mpaDao
                .findById(id)
                .orElseThrow(() -> new NotExistException("MPA with id " + id + " not exists in the DB"));
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.findAll();
    }
}
