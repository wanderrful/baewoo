package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.repository.WordRepository
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("\${routes.v1.word}")
class WordController(private val wordRepository: WordRepository) {

    @GetMapping
    fun wordGet(@RequestParam @Nullable id: String?): Flux<Word> =
        if (id != null) wordRepository.findById(id).flux()
        else wordRepository.findAll()

    // TODO | Mongo should verify that the level & word are not duplicated!
    // TODO | This should be a list, so that I can add many at once!
    @PostMapping
    fun wordPost(@RequestBody word: Word) = wordRepository.save(word)

    @DeleteMapping
    fun wordDelete(@RequestParam @Nullable id: String?): Mono<Void> =
        if (id != null) wordRepository.deleteById(id)
        else Mono.empty()

}
