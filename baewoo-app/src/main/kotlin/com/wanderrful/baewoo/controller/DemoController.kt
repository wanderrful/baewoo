package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.filter.security.JwtSigner
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DemoController(val jwtSigner: JwtSigner) {

    @GetMapping("/login")
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

        return Mono.just(output)
    }

    @PostMapping("/login")
    fun loginPost(@RequestParam username: String): String {
        return jwtSigner.createJwt(username)
    }

}
