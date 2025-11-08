package ru.yandex.practicum.filmorate.dao.reviewLikes;

public interface ReviewLikesDao {

    void addLikeReview(Long reviewId, Long userId);

    void addDislikeReview(Long reviewId, Long userId);

    void removeLikeReview(Long reviewId, Long userId);

    void removeDislikeReview(Long reviewId, Long userId);
}
