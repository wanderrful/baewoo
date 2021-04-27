package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.dao.Word
import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.dto.ReviewResult
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.service.WordConfigService
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("\${routes.v1.learn}")
class LearnController(private val wordConfigService: WordConfigService) {

    /**
     * Retrieve all available lessons.
     *  Here, "lessons" are WordConfigs with a zero rating.
     */
    @GetMapping("/lesson")
    fun lessonGet(
        @CurrentSecurityContext(expression = "authentication.principal") user: BaewooUser
    ): Flux<Word> = wordConfigService.getAvailableLessons(user.getInternalId(), user.getLevel())

    /**
     * Retrieve all available reviews.
     *  Here, "reviews" are WordConfigs with a non-zero rating.
     */
    @GetMapping("/review")
    fun reviewGet(
        @CurrentSecurityContext(expression = "authentication.principal") user: BaewooUser
    ): Flux<WordConfig> = wordConfigService.getAvailableReviews(user.getInternalId())

    /**
     * Update WordConfig ratings, based on new review outcome data.
     */
    @PostMapping("/review")
    fun reviewPost(
        @CurrentSecurityContext(expression = "authentication.principal") user: BaewooUser,
        @RequestBody reviewResults: List<ReviewResult>
    ): Flux<WordConfig> = wordConfigService.handleReviewResults(user.getInternalId(), reviewResults)

}
