package com.practice.greedygame.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article(
    val author: String? = "",
    val content: String? = "",
    val description: String? = "",
    val publishedAt: String? = "",
    val source: Source? = null,
    val title: String? = "",
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val urlToImage: String? = "",
    var isSave : Boolean?= false
)
