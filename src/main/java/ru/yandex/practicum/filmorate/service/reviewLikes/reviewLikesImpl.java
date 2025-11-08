package ru.yandex.practicum.filmorate.service.reviewLikes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.reviewLikes.ReviewLikesDao;
import ru.yandex.practicum.filmorate.dao.reviews.ReviewDao;

@Slf4j
@Service
@AllArgsConstructor
public class reviewLikesImpl implements reviewLikesService {

    private final ReviewDao reviewDao;
    private final ReviewLikesDao reviewLikesDao;

    @Override
    public void addLikeReview(Long reviewId, Long userId) {
        reviewLikesDao.addLikeReview(reviewId, userId);
        reviewDao.increaseUsefulReview(reviewId);
        log.info("add like review id {} user id {}", reviewId, userId);
    }

    @Override
    public void addDislikeReview(Long reviewId, Long userId) {
        reviewLikesDao.addDislikeReview(reviewId, userId);
        reviewDao.decreaseUsefulReview(reviewId);
        log.info("add dislike review id {} user id {}", reviewId, userId);
    }

    @Override
    public void removeLikeReview(Long reviewId, Long userId) {
        reviewLikesDao.removeLikeReview(reviewId, userId);
        reviewDao.decreaseUsefulReview(reviewId);
        log.info("remove like review id {} user id {}", reviewId, userId);
    }

    @Override
    public void removeDislikeReview(Long reviewId, Long userId) {
        reviewLikesDao.removeDislikeReview(reviewId, userId);
        reviewDao.increaseUsefulReview(reviewId);
        log.info("remove dislike review id {} user id {}", reviewId, userId);
    }
}