package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.MIN_ID;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreNameById(@PathVariable("id") @Min(MIN_ID) Long id) {
        return genreService.getGenreById(id);
    }
}