package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private static final long MIN_ID = 1L;

    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") @Min(MIN_ID) long id) {
        return mpaService.getMpaById(id);
    }
}