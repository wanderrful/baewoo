package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.UserInfo
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.enum.RoleAuthority
import com.wanderrful.baewoo.repository.UserInfoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
    private val userInfoRepository: UserInfoRepository,
    private val userLevelService: UserLevelService
) {

    /**
     * Get user data from the DB.
     */
    fun findByExternalId(userId: String, registerIfNotFound: Boolean = false): Mono<BaewooUser> {
        return userInfoRepository.findByExternalId(userId)
            .filter { it != null }
            // If the user exists in the DB
            .map { BaewooUser.from(it) }
    }

    /**
     * Bootstrapping for when a new user logs in for the first time.
     *
     * To Do:
     *  1.  Save the new user's data
     *  2.  Create the starting WordConfigs so that they can begin reviewing
     */
    fun register(userInfo: UserInfo): Mono<BaewooUser> {
        val newUser = UserInfo(
            id = userInfo.id,
            name = userInfo.name,
            provider = userInfo.provider,
            externalId = userInfo.externalId,
            avatarUrl = userInfo.avatarUrl,
            authorities = listOf(),
            level = userInfo.level,
            lastModifiedDate = userInfo.lastModifiedDate,
            createdDate = userInfo.createdDate
        )
        return userLevelService.unlockLevelForUser(newUser.id, 1)
            .then(userInfoRepository.save(newUser)
                .map { BaewooUser.from(it) })
    }

}
