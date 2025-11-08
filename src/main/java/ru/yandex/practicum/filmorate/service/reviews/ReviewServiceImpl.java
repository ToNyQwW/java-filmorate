package ru.yandex.practicum.filmorate.service.reviews;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewDao;
import ru.yandex.practicum.filmorate.entity.Review;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    @Override
    public Review addReview(Review review) {
        var addedReview = reviewDao.addReview(review);
        log.info("Review added: {}", addedReview);
        return addedReview;
    }

    @Override
    public Review getReview(Long id) {
        var review = reviewDao.getReview(id);

        if (review.isPresent()) {
            var findedReview = review.get();
            log.info("Review found: {}", findedReview);
            return findedReview;
        }
        log.error("Review not found");
        throw new NotFoundException("Review not found");
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, int count) {
        var filmReviews = reviewDao.getReviewsByFilmId(filmId, count);
        log.info("Number of reviews found: {}", filmReviews.size());

        return filmReviews;
    }

    @Override
    public Review updateReview(Review review) {
        var updatedReview = reviewDao.updateReview(review);
        log.info("Review updated: {}", updatedReview);

        return updatedReview;
    }

    @Override
    public void deleteReview(Long id) {
        reviewDao.deleteReview(id);
        log.info("Review with id deleted: {}", id);
    }
}