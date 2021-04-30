package com.wanderrful.baewoo.service

import com.wanderrful.baewoo.dao.WordConfig
import com.wanderrful.baewoo.entity.BaewooUser
import com.wanderrful.baewoo.enum.WordRating
import com.wanderrful.baewoo.repository.UserInfoRepository
import com.wanderrful.baewoo.repository.WordConfigRepository
import com.wanderrful.baewoo.repository.WordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserLevelService(
    private val userInfoRepository: UserInfoRepository,
    private val wordConfigRepository: WordConfigRepository,
    private val wordRepository: WordRepository
) {

    @Value("\${baewoo.requiredRatingForLevelUp}")
    private lateinit var requiredRatingForLevelUp: String

    @Value("\${baewoo.maxUserLevel}")
    private lateinit var maxUserLevel: String

    /**
     * Level up if we need to, otherwise no-op.
     */
    fun levelUp(userId: String): Mono<BaewooUser> {
        return shouldUserLevelUp(userId).flatMap {
            if (it) {
                return@flatMap userInfoRepository.findById(userId)
                    .flatMap { userInfo ->
                        val newLevel = (userInfo.level + 1).coerceIn(1, maxUserLevel.toInt())

                        if (newLevel != userInfo.level) {
                            userInfoRepository.save(userInfo.copy(
                                level = newLevel,
                                lastModifiedDate = Date()
                            ))
                        } else { Mono.just(userInfo) }
                    }
                    .map { userInfo -> BaewooUser.from(userInfo) }
            } else {
                return@flatMap Mono.empty()
            }
        }
    }

    /**
     * When a new user is registered, we need to create WordConfig records
     *  for the first wave of lessons so that they can begin learning.
     */
    fun unlockLevelForUser(userId: String, level: Int): Flux<WordConfig> {
        return wordRepository.findByLevel(level)
            .flatMap {
                wordConfigRepository.save(
                    WordConfig(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        wordId = it.id,

                        rating = WordRating.NONE.rating,
                        // Epoch time zero means it has never been reviewed
                        nextReviewDate = Date(0),

                        createdDate = Date(),
                        lastModifiedDate = Date(),
                    )
                )
            }
    }

    /**
     * Determine whether the User should be levelled up.
     */
    private fun shouldUserLevelUp(userId: String): Mono<Boolean> {
        return wordConfigRepository.findAllByUserIdAndRatingLessThan(
            userId,
            requiredRatingForLevelUp.toInt()
        )
            .count()
            .map { it == 0L }
    }

}