package com.practice.greedygame.domain.dao

import androidx.room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.practice.greedygame.domain.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :searchQuery || '%'")
    fun getSearch(searchQuery : String): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}