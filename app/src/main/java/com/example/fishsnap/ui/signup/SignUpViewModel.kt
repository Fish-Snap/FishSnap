package com.example.fishsnap.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.RegisterRequest
import com.example.fishsnap.auth.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {
    val registerResponse = MutableLiveData<Response<ApiResponse<RegisterRequest>>>()
    val errorMessage = MutableLiveData<String>()

    fun registerUser(name: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.registerUser(name, username, email, password)
                if (response.isSuccessful) {
                    registerResponse.postValue(response)
                } else {
                    errorMessage.postValue(response.message())
                }
            } catch (e: SocketTimeoutException) {
                errorMessage.postValue("Network timeout. Please try again.")
            } catch (e: Exception) {
                errorMessage.postValue("An unexpected error occurred.")
            }
        }
    }
}