package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.repository.WordConfigRepository
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("\${routes.v1.wordConfig}")
class WordConfigController(private val wordConfigRepository: WordConfigRepository) {

    @GetMapping
    fun wordConfigGet(@RequestParam userId: String): Flux<WordConfig> = wordConfigRepository.findByUserId(userId)

    @PostMapping
    fun wordConfigPost(@RequestBody wordConfig: WordConfig) = wordConfigRepository.save(wordConfig)

    @DeleteMapping
    fun wordConfigDelete(@RequestParam id: String) = wordConfigRepository.deleteById(id)

}
