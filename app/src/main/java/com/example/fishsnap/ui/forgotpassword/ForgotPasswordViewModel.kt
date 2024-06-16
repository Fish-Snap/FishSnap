package com.example.fishsnap.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiClient
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.forgotPassword(ForgotPasswordRequest(email))
                if (response.isSuccessful && response.body() != null) {
                    _successMessage.value = "Password reset link sent to your email"
                } else {
                    _errorMessage.value = response.body()?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Network error"
            }
        }
    }
}

data class ForgotPasswordRequest(
    val email: String
)