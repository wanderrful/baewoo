package com.wanderrful.baewoo.entity

import com.wanderrful.baewoo.dao.UserInfo
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import java.util.*

/**
 * User Entity that bridges the gap between OAuth2User DTO and UserInfo DAO.
 */
data class BaewooUser(
    private val name: String,
    private val attributes: MutableMap<String, Any>,
    private val authorities: MutableCollection<out GrantedAuthority>,

    // Metadata
    @LastModifiedDate val lastModifiedDate: Date,
    @CreatedDate val createdDate: Date,
) : OAuth2User {

    override fun getName(): String = name

    override fun getAttributes(): MutableMap<String, Any> = attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    fun getInternalId() = attributes[AttributeKey.INTERNAL_ID.key] as String

    fun getExternalId() = attributes[AttributeKey.EXTERNAL_ID.key] as String

    fun getLevel() = attributes[AttributeKey.LEVEL.key] as Int

    fun getProvider() = attributes[AttributeKey.PROVIDER.key] as String

    fun getAvatarUrl() = attributes[BaewooUser.AttributeKey.AVATAR_URL.key] as? String

    companion object {

        /**
         * Create new OAuth2-compatible user.
         *  Used when a new user logs in for the first time.
         */
        fun from(oauth2User: OAuth2User): BaewooUser = BaewooUser(
            name = oauth2User.attributes[AttributeKey.NAME.key].toString(),
            attributes = mapOAuth2UserAttributes(oauth2User),
            authorities = mapOAuth2UserAuthorities(),

            createdDate = Date(),
            lastModifiedDate = Date()
        )

        /**
         * Translate OAuth2-compatible DTO to DAO
         */
        fun from(userInfo: UserInfo): BaewooUser = BaewooUser(
            name = userInfo.name,
            attributes = mutableMapOf(
                AttributeKey.INTERNAL_ID.key to userInfo.id,
                AttributeKey.EXTERNAL_ID.key to userInfo.externalId,
                AttributeKey.LEVEL.key to userInfo.level,
                AttributeKey.PROVIDER.key to userInfo.provider),
            authorities = mutableListOf(),
            lastModifiedDate = userInfo.lastModifiedDate,

            createdDate = userInfo.createdDate)

        private fun mapOAuth2UserAttributes(
            oauth2User: OAuth2User
        ): MutableMap<String, Any> {
            // TODO | Abstract the provider when incorporating more Auth providers
            return mutableMapOf(
                AttributeKey.INTERNAL_ID.key to UUID.randomUUID().toString(),
                AttributeKey.EXTERNAL_ID.key to oauth2User.attributes["id"].toString(),
                AttributeKey.LEVEL.key to 1,
                AttributeKey.PROVIDER.key to "github"
            )
        }

        private fun mapOAuth2UserAuthorities() = mutableListOf(
            // TODO | Add non-default authorities
            OAuth2UserAuthority(
                mutableMapOf("myKey" to "myValue") as Map<String, Any>?
            )
        )

    }

    enum class AttributeKey(val key: String) {
        NAME("name"),
        EXTERNAL_ID("externalId"),
        INTERNAL_ID("internalId"),
        LEVEL("level"),
        PROVIDER("provider"),
        AVATAR_URL("avatarUrl"),
    }

}
