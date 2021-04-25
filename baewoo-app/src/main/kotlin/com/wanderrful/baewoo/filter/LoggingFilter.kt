package com.wanderrful.baewoo.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class LoggingFilter : WebFilter {

    private val log: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        log.info("message=incomingRequest path=${exchange.request.path}")

        // Continue to the next filter in the chain
        return chain.filter(exchange)
    }

}