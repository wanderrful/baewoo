package com.wanderrful.baewoo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DemoController {

    var log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/")
    fun userGet(request: ServerHttpRequest,
                @CurrentSecurityContext(expression = "authentication") auth: Authentication?)
    : Mono<String> {
        var output: String = "path=${request.path} " +
                "cookies=${request.cookies} " +
                "id=${request.id}"


        if (auth != null) {
            output =
                "$output authDetails=${auth.details} " +
                        "credentials=${auth.credentials} " +
                        "isAuth=${auth.isAuthenticated} " +
                        "authorities=${auth.authorities} " +
                        "name=${auth.name} principal=${auth.principal}"
        }

        log.info(output)

        return Mono.just(output)
    }

}
