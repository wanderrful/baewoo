package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.Word
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface WordRepository : ReactiveCrudRepository<Word, String> {

    fun findByLevel(level: Int): Flux<Word>

    fun findByLevelLessThan(level: Int): Flux<Word>

}