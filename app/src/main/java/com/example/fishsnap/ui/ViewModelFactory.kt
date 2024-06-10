package com.example.fishsnap.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishsnap.auth.ApiService
import com.example.fishsnap.auth.repository.AuthRepository
import com.example.fishsnap.ui.signup.SignUpViewModel
import com.example.fishsnap.ui.singin.SignInViewModel

class ViewModelFactory(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(AuthRepository(apiService), sharedPreferences) as T
        } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(AuthRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}