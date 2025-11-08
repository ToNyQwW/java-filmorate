package ru.yandex.practicum.filmorate.dao.reviews;

import ru.yandex.practicum.filmorate.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    Review addReview(Review review);

    Optional<Review> getReviewById(Long id);

    List<Review> getReviewsWithCount(int count);

    List<Review> getReviewsByFilmId(Long filmId, int count);

    Review updateReview(Review review);

    void increaseUsefulReview(Long reviewId);

    void decreaseUsefulReview(Long reviewId);

    void deleteReview(Long id);
}