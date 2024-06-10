package com.example.fishsnap.ui.singin

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.LoginResponse
import com.example.fishsnap.auth.UserResponse
//import com.example.fishsnap.auth.User
import com.example.fishsnap.auth.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class SignInViewModel(private val repository: AuthRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {
    val loginResponse = MutableLiveData<Response<ApiResponse<LoginResponse>>>()
    val userResponse = MutableLiveData<Response<ApiResponse<UserResponse>>>()
    val errorMessage = MutableLiveData<String>()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.loginUser(email, password)
                if (response.isSuccessful) {
                    val loginData = response.body()?.data
                    val token = loginData?.token ?: ""
                    Log.d("SignInViewModel", "Login successful, token: $token")
                    saveToken(token)
                    fetchUserData(token)
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

    private fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("TOKEN", token)
            apply()
        }
        Log.d("SignInViewModel", "Token saved: $token")
    }

    private fun fetchUserData(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserData("Bearer $token")
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    Log.d("SignInViewModel", "User data fetched: $userData")
                    saveUserData(userData)
                    userResponse.postValue(response)
                } else {
                    errorMessage.postValue(response.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("An unexpected error occurred.")
            }
        }
    }

    private fun saveUserData(user: UserResponse?) {
        with(sharedPreferences.edit()) {
            putString("USER_ID", user?.id)
            putString("USER_NAME", user?.name)
            putString("USER_ROLE", user?.role)
            putString("USER_USERNAME", user?.username)
            putString("USER_EMAIL", user?.email)
            apply()
        }
        Log.d("SignInViewModel", "User data saved: $user")
    }
}