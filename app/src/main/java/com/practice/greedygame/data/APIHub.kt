package com.practice.greedygame.data

import com.practice.greedygame.domain.model.NewsResponse
import com.practice.greedygame.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIHub {

    @GET("v2/top-headlines?country=us")
    suspend fun getHeadlines(
//        @Query("country")
//        countryCode: String = "us",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun sortByDate(
        @Query("q")
        searchQuery: String,
        @Query("from")
        from: String,
        @Query("to")
        to: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun sortBypublish(
        @Query("q")
        searchQuery: String,
        @Query("sortBy")
        sortby: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}
