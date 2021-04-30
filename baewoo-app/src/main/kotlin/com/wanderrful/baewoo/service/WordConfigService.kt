package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.dto.ReviewResult
import com.wanderrful.baewoo.enum.WordRating
import com.wanderrful.baewoo.repository.WordConfigRepository
import com.wanderrful.baewoo.repository.WordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class WordConfigService(
    private val wordConfigRepository: WordConfigRepository,
    private val wordRepository: WordRepository,
    private val userLevelService: UserLevelService
) {

    @Value("\${baewoo.ratingModifierCorrect}")
    private lateinit var ratingModifierCorrect: String

    @Value("\${baewoo.ratingModifierWrong}")
    private lateinit var ratingModifierWrong: String



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
            .map { wordRepository.findAllById(it) }
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
                    .flatMap { wordConfig ->
                        userLevelService.levelUp(userId)
                            .map { wordConfig }
                            .switchIfEmpty { Mono.just(wordConfig) }
                    }
            }
    }

    private fun getNewRating(rating: Int, wasCorrect: Boolean): Int {
        val maxRating = WordRating.values().size - 1

        // If correct, go to next level. If wrong, go down 2 levels
        val modifier = if (wasCorrect) ratingModifierCorrect.toInt() else ratingModifierWrong.toInt()

        return (rating + modifier).coerceIn(1, maxRating)
    }

    private fun getNextReviewDate(rating: Int): Date {
        val maxRating = WordRating.values().size - 1

        val newRating = rating.coerceIn(1, maxRating)

        return Date(Date().time + WordRating.values()[newRating].interval)
    }

}
