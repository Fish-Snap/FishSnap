package com.example.fishsnap.ui.detection

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishsnap.auth.ApiResponse
import com.example.fishsnap.auth.FishScanResponse
import com.example.fishsnap.auth.repository.FishRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class DetectionViewModel(private val repository: FishRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {

    val scanResponse = MutableLiveData<Response<ApiResponse<FishScanResponse>>>()
    val errorMessage = MutableLiveData<String>()

    fun scanFish(file: MultipartBody.Part, token: String, annotatedImagePath: String) {
        viewModelScope.launch {
            try {
                val response = repository.scanFish(file, token, annotatedImagePath)
                if (response.isSuccessful) {
                    // Update the response to include the annotated image path
                    val fishScanResponse = response.body()?.data
                    fishScanResponse?.let {
                        it.urlImg = annotatedImagePath // Update urlImg with the path of the annotated image
                    }
                    scanResponse.postValue(response)
                } else {
                    Log.e("DetectionViewModel", "Scan failed: Ikan tidak terdeteksi")
                    errorMessage.postValue("Scan failed: Ikan tidak terdeteksi")
                }
            } catch (e: Exception) {
                Log.e("DetectionViewModel", "An unexpected error occurred: ${e.localizedMessage}", e)
                errorMessage.postValue("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }
}
