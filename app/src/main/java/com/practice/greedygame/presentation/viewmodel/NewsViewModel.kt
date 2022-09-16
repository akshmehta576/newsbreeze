package com.practice.greedygame.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.greedygame.domain.model.Article
import com.practice.greedygame.domain.model.NewsResponse
import com.practice.greedygame.domain.repository.NewsRepository
import com.practice.greedygame.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _listofNewsState = MutableStateFlow<NewsListState>(NewsListState.Empty)
    val listofNewsState: StateFlow<NewsListState> = _listofNewsState.asStateFlow()

    private val _listofSearchState =
        MutableStateFlow<SearchNewsListState>(SearchNewsListState.Empty)
    val listofSearchState: StateFlow<SearchNewsListState> = _listofSearchState.asStateFlow()

    private val _listofDateNewsState =
        MutableStateFlow<SearchDateNewsListState>(SearchDateNewsListState.Empty)
    val listofDateNewsState: StateFlow<SearchDateNewsListState> = _listofDateNewsState.asStateFlow()

    private val _listofpublishNewsState =
        MutableStateFlow<SearchPublishNewsListState>(SearchPublishNewsListState.Empty)
    val listofpublishNewsState: StateFlow<SearchPublishNewsListState> =
        _listofpublishNewsState.asStateFlow()


    //breaking top headline
    fun listofSearchNews(country: String, pageNum: Int, apikey: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                when (val response = newsRepository.getNews()) {
                    is Resources.Success -> {

                        Log.i("transactdta", response.data.toString())
                        if (response.data != null) {
                            _listofNewsState.value = NewsListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {
                        _listofNewsState.value =
                            NewsListState.Error(response.message.toString())

                    }

                    else -> {}
                }


            } catch (e: Exception) {
                _listofNewsState.value = NewsListState.Error(e.message.toString())
            }
        }
    }


    //news by search
    fun searchlistNews(query: String, pageNum: Int, apikey: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val response = newsRepository.searchNews(query, pageNum)) {
                    is Resources.Success -> {

                        Log.i("transactdtasearch", response.data.toString())
                        if (response.data != null) {
                            _listofSearchState.value = SearchNewsListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

//                        Log.i("transactdta", "response.data?.responseData.toString()")
                        _listofSearchState.value =
                            SearchNewsListState.Error(response.message.toString())

                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _listofSearchState.value = SearchNewsListState.Error(e.message.toString())
            }
        }
    }

    //news sorted by date
    fun sortDatelistNews(query: String, from: String, to: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("linkis0", "$from $to")
                when (val response = newsRepository.sortbyDateNews(query, from, to)) {
                    is Resources.Success -> {

                        Log.i("transactdtasortsearch", response.data.toString())
                        if (response.data != null) {
                            _listofDateNewsState.value =
                                SearchDateNewsListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

//                        Log.i("transactdta", "response.data?.responseData.toString()")
                        _listofDateNewsState.value =
                            SearchDateNewsListState.Error(response.message.toString())

                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _listofDateNewsState.value = SearchDateNewsListState.Error(e.message.toString())
            }
        }
    }

    //news sorted by publish
    fun sortPublishlistNews(query: String, sortBy: String, apiKey: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("linkis0", "$sortBy $apiKey")
                when (val response = newsRepository.sortbyPublishNews(query, sortBy, apiKey)) {
                    is Resources.Success -> {

                        Log.i("transactdtasortsearch", response.data.toString())
                        if (response.data != null) {
                            _listofpublishNewsState.value =
                                SearchPublishNewsListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

//                        Log.i("transactdta", "response.data?.responseData.toString()")
                        _listofpublishNewsState.value =
                            SearchPublishNewsListState.Error(response.message.toString())

                    }

                    else -> {}
                }
            } catch (e: Exception) {
                _listofpublishNewsState.value =
                    SearchPublishNewsListState.Error(e.message.toString())
            }
        }
    }


    //room database functions
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun searchArticle(searchQuery: String) =
        newsRepository.getSearch(searchQuery)

    fun getSavedNews() = newsRepository.getSavedNews()


    sealed class NewsListState {
        object Loading : NewsListState()
        data class Success(
            val data: NewsResponse
        ) : NewsListState()

        data class Error(val message: String) : NewsListState()
        object Empty : NewsListState()
    }

    sealed class SearchNewsListState {
        object Loading : SearchNewsListState()
        data class Success(
            val data: NewsResponse
        ) : SearchNewsListState()

        data class Error(val message: String) : SearchNewsListState()
        object Empty : SearchNewsListState()
    }

    sealed class SearchDateNewsListState {
        object Loading : SearchDateNewsListState()
        data class Success(
            val data: NewsResponse
        ) : SearchDateNewsListState()

        data class Error(val message: String) : SearchDateNewsListState()
        object Empty : SearchDateNewsListState()
    }

    sealed class SearchPublishNewsListState {
        object Loading : SearchPublishNewsListState()
        data class Success(
            val data: NewsResponse
        ) : SearchPublishNewsListState()

        data class Error(val message: String) : SearchPublishNewsListState()
        object Empty : SearchPublishNewsListState()
    }
}
