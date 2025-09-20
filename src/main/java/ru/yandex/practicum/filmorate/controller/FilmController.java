package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    private static final String DEFAULT_COUNT_POPULAR_FILMS = "10";
    private static final Long MIN_ID = 1L;
    private static final Long MIN_COUNT = 1L;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable @Min(MIN_ID) Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable @Min(MIN_ID) Long filmId, @PathVariable @Min(MIN_ID) Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable @Min(MIN_ID) Long filmId, @PathVariable @Min(MIN_ID) Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(name = "count", defaultValue = DEFAULT_COUNT_POPULAR_FILMS) @Min(MIN_COUNT) Long count) {
        return filmService.getPopularFilms(count);
    }
}
