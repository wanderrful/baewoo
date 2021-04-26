package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.UserInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserInfoRepository : ReactiveCrudRepository<UserInfo, String> {

    fun findByExternalId(externalId: String): Mono<UserInfo>

}
