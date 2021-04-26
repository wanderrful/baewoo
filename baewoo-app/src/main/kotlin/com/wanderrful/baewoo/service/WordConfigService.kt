package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.dao.WordConfig
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
    fun unlockLevel(userId: String, level: Int): Flux<WordConfig> {
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
            .map {
                it.wordId
            }
            .collectList()
            .map {
                wordService.getAllWordsInList(it)
            }
            .flux()
            .flatMap { it }
    }

}
