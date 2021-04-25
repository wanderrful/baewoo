package com.wanderrful.baewoo.config

import com.wanderrful.baewoo.filter.LoggingFilter
import com.wanderrful.baewoo.filter.security.JwtSigner
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.*
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Value("\${routes.auth.whitelist}")
    private lateinit var authWhitelist: Array<String>

    @Value("\${routes.v1.word}")
    private lateinit var routeWordV1: String

    @Value("\${routes.v1.userInfo}")
    private lateinit var routeUserInfoV1: String

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity,
                               jwtAuthenticationManager: ReactiveAuthenticationManager,
                               jwtAuthenticationConverter: ServerAuthenticationConverter
    ): SecurityWebFilterChain {
        val loggingFilter = LoggingFilter()

        val jwtAuthFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        jwtAuthFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)

        return http
            // Define route authentication rules
            .authorizeExchange()
//        .pathMatchers(HttpMethod.GET, routeUserInfoV1).hasRole("USER")
//        .pathMatchers(HttpMethod.POST, routeWordV1).hasRole("MANAGER")
//        .pathMatchers(HttpMethod.DELETE, routeWordV1).hasRole("MANAGER")
//        .pathMatchers(HttpMethod.GET, *authWhitelist).permitAll()
            .pathMatchers(HttpMethod.POST, "/login").permitAll()
            .anyExchange().authenticated()
            .and()

            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()

            // Add web filters
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(loggingFilter, SecurityWebFiltersOrder.LAST)

            .build()
    }

    @Bean
    fun jwtAuthenticationConverter(jwtSigner: JwtSigner): ServerAuthenticationConverter {
        return ServerAuthenticationConverter { exchange ->
            Mono.justOrEmpty(exchange)
                .flatMap { it -> Mono
                    .justOrEmpty(it.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull())
                    .filter { it.startsWith("Bearer ") }
                    .map { it.substringAfter("Bearer ") }
                }
                .filter { it.isNotEmpty() }
                .map { jwtSigner.validateJwt(it) }
                .onErrorResume { Mono.empty() }
                .map {
                    UsernamePasswordAuthenticationToken(it.body.subject, it.body)
                        .apply { details = "myDetails" }
                }
        }
    }

    @Bean
    fun jwtAuthenticationManager(): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { authentication ->
            Mono.justOrEmpty(authentication)
                .map { token ->
                    UsernamePasswordAuthenticationToken(
                        token.principal, // TODO | This should be userId, not name
                        token.credentials,  // TODO | This shouldn't be the entire JWT body object

                        // TODO | Resolve permissions for user and populate authorities
                        mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                    ).apply { details = token.details }
                }
        }
    }

    @Bean
    fun userDetailsService(): ReactiveUserDetailsService = MapReactiveUserDetailsService(
        User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build(),
        User.withDefaultPasswordEncoder()
            .username("myAdmin")
            .password("myAdmin")
            .roles("USER", "MANAGER")
            .build()
    )

}
