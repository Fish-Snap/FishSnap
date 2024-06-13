package com.example.fishsnap.auth.repository

import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.ApiService
import com.example.fishsnap.auth.FishScanResponse
import okhttp3.MultipartBody
import retrofit2.Response

class FishRepository(private val apiService: ApiService) {

    suspend fun scanFish(file: MultipartBody.Part, token: String, annotatedImagePath: String): Response<ApiResponse<FishScanResponse>> {
        return apiService.scanFish(file, token, annotatedImagePath)
    }
}