package ru.yandex.practicum.filmorate.service.reviewLikes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviewLikes.ReviewLikesDao;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewDao;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewLikesImpl implements ReviewLikesService {

    private final ReviewDao reviewDao;
    private final ReviewLikesDao reviewLikesDao;

    @Override
    public void addLikeReview(Long reviewId, Long userId) {
        if (reviewLikesDao.hasReactionToReview(reviewId, userId, true)) {
            log.info("User {} already liked review {}", userId, reviewId);
        }

        if (reviewLikesDao.hasReactionToReview(reviewId, userId, false)) {
            reviewLikesDao.removeDislikeReview(reviewId, userId);
            reviewDao.increaseUsefulReview(reviewId, 2);
        } else {
            reviewLikesDao.addLikeReview(reviewId, userId);
            reviewDao.increaseUsefulReview(reviewId, 1);
        }
        log.info("add like review id {} user id {}", reviewId, userId);
    }

    public void addDislikeReview(Long reviewId, Long userId) {
        if (reviewLikesDao.hasReactionToReview(reviewId, userId, false)) {
            log.info("User {} already disliked review {}", userId, reviewId);
        }

        if (reviewLikesDao.hasReactionToReview(reviewId, userId, true)) {
            reviewLikesDao.removeLikeReview(reviewId, userId);
            reviewDao.decreaseUsefulReview(reviewId, 2);
        } else {
            reviewLikesDao.addDislikeReview(reviewId, userId);
            reviewDao.decreaseUsefulReview(reviewId, 1);
        }
        log.info("add dislike review id {} user id {}", reviewId, userId);
    }

    @Override
    public void removeLikeReview(Long reviewId, Long userId) {
        if (reviewLikesDao.hasReactionToReview(reviewId, userId, true)) {
            reviewLikesDao.removeLikeReview(reviewId, userId);
            reviewDao.decreaseUsefulReview(reviewId, 1);
            log.info("Removed like: reviewId={} userId={}", reviewId, userId);
        } else {
            log.info("No like to remove for reviewId={} userId={}", reviewId, userId);
        }
    }

    @Override
    public void removeDislikeReview(Long reviewId, Long userId) {
        if (reviewLikesDao.hasReactionToReview(reviewId, userId, false)) {
            reviewLikesDao.removeDislikeReview(reviewId, userId);
            reviewDao.increaseUsefulReview(reviewId, 1);
            log.info("Removed dislike: reviewId={} userId={}", reviewId, userId);
        } else {
            log.info("No dislike to remove for reviewId={} userId={}", reviewId, userId);
        }
    }
}