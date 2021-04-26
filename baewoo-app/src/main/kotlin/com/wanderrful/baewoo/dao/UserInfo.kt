package com.wanderrful.baewoo.dao

import com.wanderrful.baewoo.entity.BaewooUser
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "userInfo")
data class UserInfo(
    @Id val id: String,

    // Identity-related details
    val name: String,
    val provider: String,  // OAuth2 provider used to login
    val externalId: String,  // UserId from the provider's platform
    val avatarUrl: String?,  // Populated if this user's provider shares an avatar image with us

    // Baewoo-related details
    val level: Int,

    // Metadata
    @LastModifiedDate val lastModifiedDate: Date,
    @CreatedDate val createdDate: Date
) {
    companion object {

        /**
         * Create a new UserInfo object from OAuth2 data, for registering new users.
         */
        fun from(baewooUser: BaewooUser): UserInfo = UserInfo(
            id = baewooUser.attributes[BaewooUser.AttributeKey.INTERNAL_ID.key] as String,

            name = baewooUser.name,
            provider = baewooUser.attributes[BaewooUser.AttributeKey.PROVIDER.key] as String,
            externalId = baewooUser.attributes[BaewooUser.AttributeKey.EXTERNAL_ID.key] as String,
            avatarUrl = baewooUser.attributes[BaewooUser.AttributeKey.AVATAR_URL.key] as? String,

            level = baewooUser.attributes[BaewooUser.AttributeKey.LEVEL.key] as Int,

            lastModifiedDate = baewooUser.lastModifiedDate,
            createdDate = baewooUser.createdDate,
        )

    }
}
