package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Director;
import ru.yandex.practicum.filmorate.service.directors.DirectorsService;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.MIN_ID;

@RestController
@AllArgsConstructor
@RequestMapping("/directors")
public class DirectorsController {

    private final DirectorsService directorService;

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable @Min(MIN_ID) Long id) {
        return directorService.getDirectorById(id);
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable @Min(MIN_ID) Long id) {
        directorService.deleteDirectorById(id);
    }
}