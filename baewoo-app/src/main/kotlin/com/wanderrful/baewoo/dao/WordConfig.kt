package com.wanderrful.baewoo.dao

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "wordConfig")
data class WordConfig(
    @Id val id: String,
    val userId: String,
    val wordId: String,
    val rating: Int,

    val nextReviewDate: Date,  // Epoch time zero means this has never been reviewed

    // Metadata
    @CreatedDate val createdDate: Date,
    @LastModifiedDate val lastModifiedDate: Date,
)
