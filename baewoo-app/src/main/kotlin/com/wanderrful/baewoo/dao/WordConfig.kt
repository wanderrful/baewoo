package com.wanderrful.baewoo.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "wordConfig")
data class WordConfig(
    @Id val id: String,
    val userId: String,
    val wordId: String,
    val rating: String,
    val lastReviewed: Date,
)
