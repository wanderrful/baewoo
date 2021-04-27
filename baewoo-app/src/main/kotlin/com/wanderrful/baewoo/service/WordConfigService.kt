package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.dto.ReviewResult
import com.wanderrful.baewoo.enum.WordRating
import com.wanderrful.baewoo.repository.WordConfigRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

@Service
class WordConfigService(
    private val wordConfigRepository: WordConfigRepository,
    private val wordService: WordService
) {

    /**
     * When a new user is registered, we need to create WordConfig records
     *  for the first wave of lessons so that they can begin learning.
     */
    fun unlockLevelForUser(userId: String, level: Int): Flux<WordConfig> {
        return wordService.getAllWordsForLevel(level)
            .flatMap {
                wordConfigRepository.save(WordConfig(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    wordId = it.id,
                    rating = WordRating.NONE.rating,
                    nextReviewDate = Date(0),  // Epoch time zero means it has never been reviewed
                    createdDate = Date(),
                    lastModifiedDate = Date(),
                ))
            }
    }

    /**
     * Get available reviews (i.e. rating is not NONE and nextReviewDate is in the past).
     */
    fun getAvailableReviews(userId: String): Flux<WordConfig> {
        return wordConfigRepository.findReviewsByUserId(userId)
    }

    /**
     * Get all words that have not yet been reviewed (i.e. have a rating of NONE).
     */
    fun getAvailableLessons(userId: String, level: Int): Flux<Word> {
        return wordConfigRepository.findAvailableLessonsByUserId(userId)
            .map { it.wordId }
            .collectList()
            .map { wordService.getAllWordsInList(it) }
            .flux()
            .flatMap { it }
    }

    /**
     * Process results of a review for a user.
     *  For reach result...
     *  1.  Look up the WordConfig so we can see the current rating
     *  2.  Save that rating with the adjusted value
     */
    fun handleReviewResults(userId: String, results: List<ReviewResult>): Flux<WordConfig> {
        // TODO | Add behavior for leveling up the user!
        return Flux.fromIterable(results)
            .flatMap { result ->
                wordConfigRepository.findByUserIdAndWordId(userId, result.wordId)
                    .map {
                        val newRating = getNewRating(it.rating, result.wasCorrect)

                        return@map it.copy(
                            rating = newRating,
                            nextReviewDate = getNextReviewDate(newRating),
                            lastModifiedDate = Date()
                        )
                    }
                    .flatMap { wordConfigRepository.save(it) }
            }
    }

    private fun getNewRating(rating: Int, wasCorrect: Boolean): Int {
        val maxRating = WordRating.values().size - 1

        // If correct, go to next level. If wrong, go down 2 levels
        val modifier = if (wasCorrect) 1 else -2

        return (rating + modifier).coerceIn(1, maxRating)
    }

    private fun getNextReviewDate(rating: Int): Date {
        val maxRating = WordRating.values().size - 1

        val newRating = rating.coerceIn(1, maxRating)

        return Date(Date().time + WordRating.values()[newRating].interval)
    }

}
