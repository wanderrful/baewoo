package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.repository.WordRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class WordService(private val wordRepository: WordRepository) {

    fun getAllWordsForLevel(level: Int): Flux<Word> {
        return wordRepository.findByLevel(level)
    }

    fun getAllWordsInList(ids: List<String>): Flux<Word> {
        return wordRepository.findAllById(ids)
    }

}
