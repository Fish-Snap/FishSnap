package com.example.fishsnap.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.NewsItem
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> get() = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _showNoNews = MutableLiveData<Boolean>()
    val showNoNews: LiveData<Boolean> get() = _showNoNews

    val errorMessage = MutableLiveData<String>()

    fun fetchNews(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getNews("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val newsList = response.body()?.data ?: emptyList()
                    _newsList.value = newsList
                    _showNoNews.value = newsList.isEmpty()
                } else {
                    _showNoNews.value = true
                    Log.e("NewsViewModel", "Load Data failed: Tidak Ada Berita")
                    errorMessage.postValue("Load Data failed: Tidak Ada Berita")
                }
            } catch (e: Exception) {
                _showNoNews.value = true
                Log.e("NewsViewModel", "An unexpected error occurred: ${e.localizedMessage}", e)
                errorMessage.postValue("An unexpected error occurred: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
