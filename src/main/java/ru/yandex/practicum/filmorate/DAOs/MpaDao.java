package ru.yandex.practicum.filmorate.DAOs;

import ru.yandex.practicum.filmorate.models.MPA;

import java.util.List;

public interface MpaDao {
    MPA getMpaById(Long id);

    List<MPA> getMpas();
}
