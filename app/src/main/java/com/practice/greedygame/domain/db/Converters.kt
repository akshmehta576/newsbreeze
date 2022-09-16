package com.practice.greedygame.domain.db

import androidx.room.TypeConverter
import com.practice.greedygame.domain.model.Source

class Converters {

    @TypeConverter
    fun fromSource(newsArticle: Source): String {
        return newsArticle.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}