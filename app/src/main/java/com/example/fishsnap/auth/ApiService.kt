package com.example.fishsnap.auth

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<RegisterRequest>>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<LoginResponse>>

    @GET("auth/me")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserResponse>>

    @Multipart
    @POST("fish/scan")
    suspend fun scanFish(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String,
        @Part("annotatedImagePath") annotatedImagePath: String
    ): Response<ApiResponse<FishScanResponse>>

    @GET("fish/history")
    suspend fun getFishHistory(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<FishScanResponse>>>

    @GET("news/current-day")
    suspend fun getNews(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<NewsItem>>>
}