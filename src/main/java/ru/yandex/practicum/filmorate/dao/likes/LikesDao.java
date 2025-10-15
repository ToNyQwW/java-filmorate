package ru.yandex.practicum.filmorate.dao.likes;

import ru.yandex.practicum.filmorate.entity.Likes;

import java.util.List;
import java.util.Map;

public interface LikesDao {

    void addLike(Long filmId, Long userId);

    Likes getFilmLikes(Long filmId);

    Map<Long, Likes> getUserLikesByFilmIds(List<Long> filmsIds);

    List<Long> getPopularFilmIds(Long count);

    void removeLike(Long filmId, Long userId);
}