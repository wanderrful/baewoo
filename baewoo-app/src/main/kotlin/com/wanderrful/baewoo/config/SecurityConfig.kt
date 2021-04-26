package com.wanderrful.baewoo.config

import com.wanderrful.baewoo.dao.UserInfo
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.filter.LoggingFilter
import com.wanderrful.baewoo.repository.UserInfoRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.client.userinfo.*
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
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        // Define route authentication rules
        .authorizeExchange()
        .anyExchange().authenticated()
        .and()

        .csrf().disable()
        .httpBasic().disable()
        .formLogin().disable()
        .logout().disable()

        .oauth2Login()
        .and()

        // Add web filters
        .addFilterAt(LoggingFilter(), SecurityWebFiltersOrder.LAST)

        .build()

    /**
     * Leverage OAuth2 data to load our user data when they
     *  initially authenticate (i.e. no valid session data in cache).
     */
    @Bean
    fun oauth2UserService(
        userInfoRepository: UserInfoRepository
    ): ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
        val delegate = DefaultReactiveOAuth2UserService()
        return ReactiveOAuth2UserService { request ->
            delegate.loadUser(request)
                .doOnError { throw OAuth2AuthenticationException(OAuth2Error("")) }
                .flatMap { oauth2User ->
                    userInfoRepository.findByExternalId(oauth2User.attributes["id"].toString())
                        .filter { it != null }
                        .map {  // If the user exists in the DB
                            BaewooUser.from(it)
                        }
                        .switchIfEmpty(Mono.justOrEmpty(oauth2User)
                            .map {  // If the user doesn't exist in the DB
                                BaewooUser.from(it)
                            }
                            .flatMap {  // Save new UserInfo object
                                userInfoRepository.save(UserInfo.from(it))
                                    .map {  // Re-assemble BaewooUser from new UserInfo
                                        BaewooUser.from(it)
                                    }
                            })
                }
        }
    }

}
