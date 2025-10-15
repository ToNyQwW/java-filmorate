package ru.yandex.practicum.filmorate.dao.likes;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikesDao {

    void addLike(Long filmId, Long userId);

    Set<Long> getFilmLikes(Long filmId);

    Map<Long, Set<Long>> getUserLikesByFilmIds(List<Long> filmsIds);

    List<Long> getPopularFilmIds(Long count);

    void removeLike(Long filmId, Long userId);
}