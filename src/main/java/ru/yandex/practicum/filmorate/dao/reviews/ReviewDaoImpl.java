package ru.yandex.practicum.filmorate.dao.reviews;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.reviews.sql.*;
import ru.yandex.practicum.filmorate.entity.Review;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Review addReview(Review review) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("content", review.getContent())
                .addValue("isPositive", review.getIsPositive())
                .addValue("userId", review.getUserId())
                .addValue("filmId", review.getFilmId())
                .addValue("useful", review.getUseful());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(AddReviewSql.create(), params, keyHolder);

        review.setReviewId(keyHolder.getKey().longValue());

        return review;
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        var result = jdbcTemplate.query(GetReviewByIdSql.create(), reviewRowMapper, id);
        var review = DataAccessUtils.singleResult(result);

        return Optional.ofNullable(review);
    }

    @Override
    public List<Review> getReviewsWithCount(int count) {
        return jdbcTemplate.query(GetAllReviewsWithCountSql.create(), reviewRowMapper, count);
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, int count) {
        return jdbcTemplate.query(GetReviewsByFilmIdSql.create(), reviewRowMapper, filmId, count);
    }

    @Override
    public Review updateReview(Review review) {
        int updatedCount = jdbcTemplate.update(UpdateReviewSql.create(), review.getContent(),
                review.getIsPositive(),
                review.getUseful(),
                review.getReviewId());

        if (updatedCount == 0) {
            log.error("Failed to update review");
            throw new NotFoundException("review with id " + review.getReviewId() + " not found");
        }
        return review;
    }

    @Override
    public void increaseUsefulReview(Long reviewId, int delta) {
        var updatedCount = jdbcTemplate.update(IncreaseUsefulReviewSql.create(), delta, reviewId);

        if (updatedCount == 0) {
            log.error("Failed to update useful review");
            throw new NotFoundException("review with id " + reviewId + " not found");
        }
    }

    @Override
    public void decreaseUsefulReview(Long reviewId, int delta) {
        var updatedCount = jdbcTemplate.update(DecreaseUsefulReviewSql.create(), delta, reviewId);

        if (updatedCount == 0) {
            log.error("Failed to update useful review");
            throw new NotFoundException("review with id " + reviewId + " not found");
        }
    }

    @Override
    public void deleteReview(Long id) {
        var updatedCount = jdbcTemplate.update(DeleteReviewSql.create(), id);

        if (updatedCount == 0) {
            log.error("Failed to delete review");
            throw new NotFoundException("review with id " + id + " not found");
        }
    }
}