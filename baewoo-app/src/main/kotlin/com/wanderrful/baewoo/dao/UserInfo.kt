package com.wanderrful.baewoo.dao

import com.wanderrful.baewoo.entity.BaewooUser
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "userInfo")
data class UserInfo(
    @Id val id: String,

    // Identity-related details
    val name: String,
    val provider: String,  // OAuth2 provider used to login
    val externalId: String,  // UserId from the provider's platform
    val avatarUrl: String?,

    // Baewoo-related details
    val level: Int,
) {
    companion object {

        fun from(baewooUser: BaewooUser): UserInfo = UserInfo(
            id = baewooUser.attributes[BaewooUser.AttributeKey.INTERNAL_ID.key] as String,

            name = baewooUser.name,
            provider = baewooUser.attributes[BaewooUser.AttributeKey.PROVIDER.key] as String,
            externalId = baewooUser.attributes[BaewooUser.AttributeKey.EXTERNAL_ID.key] as String,
            avatarUrl = baewooUser.attributes[BaewooUser.AttributeKey.AVATAR_URL.key] as? String,

            level = baewooUser.attributes[BaewooUser.AttributeKey.LEVEL.key] as Int,
        )

    }
}
