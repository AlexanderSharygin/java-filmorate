package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.List;

@RestController
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {

        this.mpaService = mpaService;
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMPAById(@PathVariable("id") long mpaId) {
        return mpaService.getMPAById(mpaId);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMPAs() {
        return mpaService.getAllMpa();
    }
}