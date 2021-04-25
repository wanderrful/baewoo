package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.WordConfig
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WordConfigRepository : ReactiveCrudRepository<WordConfig, String> {

    fun findByUserId(userId: String): Flux<WordConfig>

    fun findByUserIdAndWordId(userId: String, wordId: String): Mono<WordConfig>

}
