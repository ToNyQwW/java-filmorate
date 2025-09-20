package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return null;
    }

    @GetMapping
    public List<Film> getFilms() {
        return null;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return null;
    }
}
