package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.service.WordConfigService
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("\${routes.v1.learn}")
class LearnController(private val wordConfigService: WordConfigService) {

    /**
     * Retrieve all available reviews.
     */
    @GetMapping("/review")
    fun reviewsGet(
        @CurrentSecurityContext(expression = "authentication.principal") user: BaewooUser
    ): Flux<WordConfig> = wordConfigService
        .getAvailableReviews(
            user.attributes[BaewooUser.AttributeKey.INTERNAL_ID.key].toString()
        )

    /**
     * Retrieve all available lessons.
     */
    @GetMapping("/lesson")
    fun lessonsGet(
        @CurrentSecurityContext(expression = "authentication.principal") user: BaewooUser
    ): Flux<Word> = wordConfigService
        .getAvailableLessons(
            user.attributes[BaewooUser.AttributeKey.INTERNAL_ID.key].toString(),
            user.attributes[BaewooUser.AttributeKey.LEVEL.key] as Int,
        )

}
