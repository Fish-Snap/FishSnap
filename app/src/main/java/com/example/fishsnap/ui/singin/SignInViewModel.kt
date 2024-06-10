package com.example.fishsnap.ui.singin

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.LoginResponse
//import com.example.fishsnap.auth.User
import com.example.fishsnap.auth.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class SignInViewModel(private val repository: AuthRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {
    val loginResponse = MutableLiveData<Response<ApiResponse>>()
    val errorMessage = MutableLiveData<String>()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.loginUser(email, password)
                if (response.isSuccessful) {
                    val loginData = response.body()?.data as Map<String, Any>
                    val token = loginData["access_token"] as String
                    saveUserData(token)
                    loginResponse.postValue(response)
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

    private fun saveUserData(token: String) {
        with(sharedPreferences.edit()) {
            putString("TOKEN", token)
            apply()
        }
    }
}