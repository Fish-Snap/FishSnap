package com.example.fishsnap.ui.history

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.FishScanResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val _historyData = MutableLiveData<List<FishScanResponse>>()
    val historyData: LiveData<List<FishScanResponse>> get() = _historyData
    val errorMessage = MutableLiveData<String>()

    fun loadHistory() {
        val token = sharedPreferences.getString("TOKEN", "") ?: ""
        if (token.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val response = ApiClient.apiService.getFishHistory("Bearer $token")
                    if (response.isSuccessful) {
                        _historyData.value = response.body()?.data?.sortedByDescending { it.createdAt } ?: emptyList()
                    } else {
                        errorMessage.postValue("History Tidak Ada")
                    }
                } catch (e: Exception) {
                    errorMessage.postValue("An unexpected error occurred: ${e.localizedMessage}")
                }
            }
        }
    }
}

