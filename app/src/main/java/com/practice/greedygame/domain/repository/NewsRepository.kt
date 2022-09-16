package com.practice.greedygame.domain.repository

import android.util.Log
import com.practice.greedygame.data.APIHub
import com.practice.greedygame.domain.db.NewsDatabase
import com.practice.greedygame.domain.model.Article
import com.practice.greedygame.domain.model.NewsResponse
import com.practice.greedygame.util.Constants.API_KEY
import com.practice.greedygame.util.Resources
import java.lang.Exception
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiHub: APIHub, private val newsDatabase: NewsDatabase)
{

    //breaking and top headlines api call
    suspend fun getNews() : Resources<NewsResponse?>
    {
        val response = try {
            apiHub.getHeadlines()
        }
        catch (e: Exception) {
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())
    }

    //news by search api call
    suspend fun searchNews(searchQuery : String, pageNum : Int) : Resources<NewsResponse?>
    {
        val response = try {
            apiHub.searchForNews(searchQuery, pageNum, API_KEY)
        }
        catch (e: Exception) {
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())
    }

    //news sorted by date api call
    suspend fun sortbyDateNews(query: String, from : String, to : String) : Resources<NewsResponse?>
    {
        val response = try {
            Log.i("sortbyDataNews", "v2/everything?q=$query&from=$from&to=$to&apiKey=$API_KEY")
            apiHub.sortByDate(query,from, to, API_KEY)
        }
        catch (e: Exception) {
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())
    }

    //news sorted by publish api call
    suspend fun sortbyPublishNews(query: String, sortby : String, apiKey : String) : Resources<NewsResponse?>
    {
        val response = try {
            Log.i("sortbyDataNews", "v2/everything?q=$query&from=$sortby&to=&apiKey=$API_KEY")
            apiHub.sortBypublish(query,sortby, API_KEY)
        }
        catch (e: Exception) {
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())
    }

    //room db
    suspend fun upsert(article: Article) = newsDatabase.getNewsDao().upsert(article)
    fun getSearch(searchQuery: String) = newsDatabase.getNewsDao().getSearch(searchQuery)
    fun getSavedNews() = newsDatabase.getNewsDao().getAllArticles()

}