package com.wanderrful.baewoo.dao

import com.wanderrful.baewoo.entity.BaewooUser
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

@Document(collection = "userInfo")
data class UserInfo(
    @Id val id: String,  // Internal ID

    // Identity-related details
    val name: String,
    val provider: String,  // OAuth2 provider used to login
    val externalId: String,  // UserId from the provider's platform
    val avatarUrl: String?,  // Populated if this user's provider shares an avatar image with us
    val authorities: List<String>,  // User's allowed authorities

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
            id = baewooUser.getInternalId(),

            name = baewooUser.name,
            provider = baewooUser.getProvider(),
            externalId = baewooUser.getExternalId(),
            avatarUrl = baewooUser.getAvatarUrl(),
            authorities = baewooUser.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()),

            level = baewooUser.getLevel(),

            lastModifiedDate = baewooUser.lastModifiedDate,
            createdDate = baewooUser.createdDate,
        )

    }
}
