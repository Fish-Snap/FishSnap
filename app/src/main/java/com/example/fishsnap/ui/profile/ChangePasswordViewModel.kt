package com.example.fishsnap.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.ChangePasswordRequest
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    private val _changePasswordResult = MutableLiveData<Boolean>()
    val changePasswordResult: LiveData<Boolean> get() = _changePasswordResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun changePassword(token: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.changePassword(
                    "Bearer $token",
                    ChangePasswordRequest(oldPassword, newPassword)
                )
                if (response.isSuccessful && response.body() != null) {
                    _changePasswordResult.value = true
                } else {
                    _errorMessage.value = response.body()?.message ?: "Unknown error"
                    _changePasswordResult.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Network error"
                _changePasswordResult.value = false
            }
        }
    }
}