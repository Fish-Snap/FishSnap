package com.example.fishsnap.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishsnap.auth.repository.AuthRepository
import com.example.fishsnap.ui.signup.SignUpViewModel
import com.example.fishsnap.ui.singin.SignInViewModel

class ViewModelFactory(private val repository: Any, private val sharedPreferences: SharedPreferences? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository as AuthRepository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository as AuthRepository, sharedPreferences!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}