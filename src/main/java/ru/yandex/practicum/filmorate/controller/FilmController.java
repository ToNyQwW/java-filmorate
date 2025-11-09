package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.model.SortType;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.*;

@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilms(
            @PathVariable @Min(MIN_ID) Long directorId, @RequestParam String sortBy) {
        return filmService.getFilmsByDirectorId(directorId, SortType.valueOf(sortBy));
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