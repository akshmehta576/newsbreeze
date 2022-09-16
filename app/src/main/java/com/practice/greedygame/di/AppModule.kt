package com.practice.greedygame.di

import android.content.Context
import com.practice.greedygame.data.APIHub
import com.practice.greedygame.domain.dao.NewsDao
import com.practice.greedygame.domain.db.NewsDatabase
import com.practice.greedygame.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getInstance() : APIHub {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client  = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIHub::class.java)
    }

    @Singleton
    @Provides
    fun getAppDatabase(@ApplicationContext context: Context) : NewsDatabase{
        return NewsDatabase.invoke(context)
    }
//
//    @Singleton
//    @Provides
//    fun getAppDao(newsDatabase: NewsDatabase) : NewsDao{
//        return newsDatabase.getNewsDao()
//    }
}