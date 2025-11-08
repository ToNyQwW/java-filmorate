package ru.yandex.practicum.filmorate.dao.reviewLikes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.reviewLikes.sql.AddDislikeReviewSql;
import ru.yandex.practicum.filmorate.dao.reviewLikes.sql.AddLikeReviewSql;
import ru.yandex.practicum.filmorate.dao.reviewLikes.sql.RemoveDislikeReviewSql;
import ru.yandex.practicum.filmorate.dao.reviewLikes.sql.RemoveLikeReviewSql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Slf4j
@Repository
@AllArgsConstructor
public class ReviewLikesDaoImpl implements ReviewLikesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeReview(Long reviewId, Long userId) {
        var updateCount = jdbcTemplate.update(AddLikeReviewSql.create(), reviewId, userId);

        if (updateCount == 0) {
            log.error("Failed add Like to review");
            throw new NotFoundException("review or user not found");
        }
    }

    @Override
    public void addDislikeReview(Long reviewId, Long userId) {
        var updateCount = jdbcTemplate.update(AddDislikeReviewSql.create(), reviewId, userId);

        if (updateCount == 0) {
            log.error("Failed add Dislike to review");
            throw new NotFoundException("review or user not found");
        }
    }

    @Override
    public void removeLikeReview(Long reviewId, Long userId) {
        var updateCount = jdbcTemplate.update(RemoveLikeReviewSql.create(), reviewId, userId);

        if (updateCount == 0) {
            log.error("Failed remove like from review");
            throw new NotFoundException("review or user not found");
        }
    }

    @Override
    public void removeDislikeReview(Long reviewId, Long userId) {
        var updateCount = jdbcTemplate.update(RemoveDislikeReviewSql.create(), reviewId, userId);

        if (updateCount == 0) {
            log.error("Failed remove dislike from review");
            throw new NotFoundException("review or user not found");
        }
    }
}