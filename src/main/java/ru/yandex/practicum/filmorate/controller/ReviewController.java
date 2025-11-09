package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Review;
import ru.yandex.practicum.filmorate.service.reviewLikes.ReviewLikesService;
import ru.yandex.practicum.filmorate.service.reviews.ReviewService;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.*;

@RestController
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewLikesService reviewLikesService;

    @GetMapping
    public List<Review> getReviews(
            @RequestParam(name = "filmId", defaultValue = DEFAULT_FILM_ID) @Min(MIN_ID) Long filmId,
            @RequestParam(name = "count", defaultValue = DEFAULT_COUNT_REVIEWS) @Min(MIN_COUNT) int count) {
        return reviewService.getReviews(filmId, count);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable @Min(MIN_ID) Long id) {
        return reviewService.getReview(id);
    }

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable @Min(MIN_ID) Long id) {
        reviewService.deleteReview(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeReview(@PathVariable @Min(MIN_ID) Long id,
                              @PathVariable @Min(MIN_ID) Long userId) {
        reviewLikesService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeReview(@PathVariable @Min(MIN_ID) Long id,
                                 @PathVariable @Min(MIN_ID) Long userId) {
        reviewLikesService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeReview(@PathVariable @Min(MIN_ID) Long id,
                                 @PathVariable @Min(MIN_ID) Long userId) {
        reviewLikesService.removeLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislikeReview(@PathVariable @Min(MIN_ID) Long id,
                                    @PathVariable @Min(MIN_ID) Long userId) {
        reviewLikesService.removeDislikeReview(id, userId);
    }
}