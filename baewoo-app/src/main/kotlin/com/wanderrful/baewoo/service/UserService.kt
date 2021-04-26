package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.UserInfo
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.repository.UserInfoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userInfoRepository: UserInfoRepository,
    private val wordConfigService: WordConfigService
) {

    /**
     * Get user data from the DB.
     */
    fun findByExternalId(userId: String, registerIfNotFound: Boolean = false): Mono<BaewooUser> {
        return userInfoRepository.findByExternalId(userId)
            .filter { it != null }
            .map {  // If the user exists in the DB
                BaewooUser.from(it)
            }
    }

    /**
     * Bootstrapping for when a new user logs in for the first time.
     *
     * To Do:
     *  1.  Save the new user's data
     *  2.  Create the starting WordConfigs so that they can begin reviewing
     */
    fun register(userInfo: UserInfo): Mono<BaewooUser> {
        return wordConfigService.unlockLevel(userInfo.id, 1)
            .then(userInfoRepository.save(userInfo)
                .map { BaewooUser.from(it) })
    }

}