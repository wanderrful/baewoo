package com.wanderrful.baewoo.dao

import com.wanderrful.baewoo.enum.WordType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "words")
data class Word(
    @Id val id: String,
    val type: WordType,
    val level: Int,
    val word: String,
    val meaning: String,
    val roots: List<String>,
)
