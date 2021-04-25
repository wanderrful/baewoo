package com.wanderrful.baewoo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${routes.v1.learn}")
class LearnController {

    /**
     * Retrieve all available reviews.
     */
    @GetMapping("/review")
    fun reviewsGet(): Nothing = throw NotImplementedError()

    /**
     * Retrieve all available lessons.
     */
    @GetMapping("/lesson")
    fun lessonsGet(): Nothing = throw NotImplementedError()

}
