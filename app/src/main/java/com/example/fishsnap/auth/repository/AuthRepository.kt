package com.example.fishsnap.auth.repository

import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.ApiService
import com.example.fishsnap.auth.LoginRequest
import com.example.fishsnap.auth.LoginResponse
import com.example.fishsnap.auth.RegisterRequest
import com.example.fishsnap.auth.UserResponse
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    suspend fun loginUser(email: String, password: String): Response<ApiResponse<LoginResponse>> {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun getUserData(token: String): Response<ApiResponse<UserResponse>> {
        return apiService.getUserData(token)
    }

    suspend fun registerUser(
        email: String,
        username: String,
        password: String,
        name: String
    ): Response<ApiResponse<RegisterRequest>> {
        val request = RegisterRequest(email, username, password, name)
        return apiService.register(request)
    }
}