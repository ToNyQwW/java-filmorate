package ru.yandex.practicum.filmorate.service.reviewLikes;

public interface reviewLikesService {

    void addLikeReview(Long reviewId, Long userId);

    void addDislikeReview(Long reviewId, Long userId);

    void removeLikeReview(Long reviewId, Long userId);

    void removeDislikeReview(Long reviewId, Long userId);
}