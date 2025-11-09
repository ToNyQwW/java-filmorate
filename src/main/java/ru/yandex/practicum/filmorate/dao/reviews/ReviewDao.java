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

    void increaseUsefulReview(Long reviewId, int delta);

    void decreaseUsefulReview(Long reviewId, int delta);

    void deleteReview(Long id);
}