package ru.yandex.practicum.filmorate.service.reviews;

import ru.yandex.practicum.filmorate.entity.Review;

import java.util.List;

public interface ReviewService {

    Review addReview(Review review);

    Review getReview(Long id);

    List<Review> getReviews(Long filmId, int count);

    Review updateReview(Review review);

    void deleteReview(Long id);
}