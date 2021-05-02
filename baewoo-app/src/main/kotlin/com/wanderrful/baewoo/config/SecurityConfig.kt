package com.wanderrful.baewoo.config

import com.wanderrful.baewoo.dao.UserInfo
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.enum.RoleAuthority
import com.wanderrful.baewoo.filter.LoggingFilter
import com.wanderrful.baewoo.repository.UserInfoRepository
import com.wanderrful.baewoo.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
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
class SecurityConfig(private val userService: UserService) {

    @Value("\${routes.auth.whitelist}")
    private lateinit var authWhitelist: Array<String>

    @Value("\${routes.v1.word}")
    private lateinit var routeWordV1: String

    @Value("\${routes.v1.userInfo}")
    private lateinit var routeUserInfoV1: String

    @Value("\${routes.v1.learn}")
    private lateinit var routeLearnV1: String

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .authorizeExchange()
            .pathMatchers(HttpMethod.POST, routeWordV1)
                .hasRole(getAdjustedRoleName(RoleAuthority.ROLE_CREATE_WORDS))
            .pathMatchers(HttpMethod.DELETE, routeWordV1)
                .hasRole(getAdjustedRoleName(RoleAuthority.ROLE_DELETE_WORDS))
            .pathMatchers(HttpMethod.POST, routeUserInfoV1)
                .hasRole(getAdjustedRoleName(RoleAuthority.ROLE_MANAGE_USERS))
            .pathMatchers(HttpMethod.DELETE, routeUserInfoV1)
                .hasRole(getAdjustedRoleName(RoleAuthority.ROLE_MANAGE_USERS))
            .pathMatchers(HttpMethod.GET, *authWhitelist).permitAll()
            .pathMatchers(HttpMethod.GET, "/").permitAll()
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
                    userService.findByExternalId(oauth2User.attributes["id"].toString())
                        // If this user doesn't exist
                        .switchIfEmpty(Mono.justOrEmpty(oauth2User)
                            // Register new user
                            .map {  BaewooUser.from(it) }
                            .flatMap {  userService.register(UserInfo.from(it)) })
                }
        }
    }

    private fun getAdjustedRoleName(role: RoleAuthority): String = role.toString().substringAfter("ROLE_")

}
