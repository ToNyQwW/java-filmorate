package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Integer, User> users = new HashMap<>();

    private int id;


    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(++id);
        normalize(user);
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            log.warn("User not found: {}", userId);
            throw new NotFoundException("User not found");
        }
        normalize(user);
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return user;
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
