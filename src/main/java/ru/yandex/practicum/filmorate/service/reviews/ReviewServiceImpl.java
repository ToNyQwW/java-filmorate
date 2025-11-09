package ru.yandex.practicum.filmorate.service.reviews;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewDao;
import ru.yandex.practicum.filmorate.entity.Review;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final UserService userService;
    private final FilmService filmService;

    @Override
    public Review addReview(Review review) {

        if (userService.getUser(review.getUserId()) == null) {
            throw new NotFoundException("User with id " + review.getUserId() + " not found");
        }
        if (filmService.getFilm(review.getFilmId()) == null) {
            throw new NotFoundException("Film with id " + review.getFilmId() + " not found");
        }

        var addedReview = reviewDao.addReview(review);
        log.info("Review added: {}", addedReview);
        return addedReview;
    }

    @Override
    public Review getReview(Long id) {
        var review = reviewDao.getReviewById(id);

        if (review.isPresent()) {
            var findedReview = review.get();
            log.info("Review found: {}", findedReview);
            return findedReview;
        }
        log.error("Review not found");
        throw new NotFoundException("Review not found");
    }

    @Override
    public List<Review> getReviews(Long filmId, int count) {
        List<Review> reviews;
        if (filmId == 0) {
            reviews = reviewDao.getReviewsWithCount(count);
        } else {
            reviews = reviewDao.getReviewsByFilmId(filmId, count);
        }
        log.info("Number of reviews found: {}", reviews.size());

        return reviews;
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