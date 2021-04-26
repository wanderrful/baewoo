package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.WordConfig
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WordConfigRepository : ReactiveCrudRepository<WordConfig, String> {

    /**
     * Get data about all words that this user has reviewed.
     */
    fun findByUserId(userId: String): Flux<WordConfig>

    /**
     * Get all words that are now eligible for review.
     *  We want all wordConfigs for this user, where:
     *  - `nextReviewDate` is in the past
     *  - The rating is greater than zero (i.e. "NONE").
     */
    @Query("{ 'userId': ?0, 'nextReviewDate': { \$lt: new Date() }, rating: { \$gt: 0 } }")
    fun findReviewsByUserId(userId: String): Flux<WordConfig>

    /**
     * Get all WordConfigs that have never been reviewed.
     *  Rating zero (i.e. "NONE") is our source of truth.
     */
    @Query("{ 'userId': ?0, 'rating': 0 }")
    fun findAvailableLessonsByUserId(userId: String): Flux<WordConfig>

}
