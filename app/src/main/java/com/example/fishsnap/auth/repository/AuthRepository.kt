package com.example.fishsnap.auth.repository

import com.example.fishsnap.auth.ApiClient
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.LoginRequest
import com.example.fishsnap.auth.RegisterRequest
import com.example.fishsnap.auth.UserResponse
import retrofit2.Response

class AuthRepository {
    private val apiService = ApiClient.apiService

    suspend fun registerUser(name: String, username: String, email: String, password: String) =
        apiService.register(RegisterRequest(name, username, email, password))

    suspend fun loginUser(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    suspend fun getUserData(token: String): Response<ApiResponse<UserResponse>> {
        return apiService.getUserData(token)
    }
}