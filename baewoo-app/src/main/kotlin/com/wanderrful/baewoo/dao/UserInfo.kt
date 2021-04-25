package com.wanderrful.baewoo.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "userInfo")
data class UserInfo(
    @Id val id: String,
    val level: Int,
)
