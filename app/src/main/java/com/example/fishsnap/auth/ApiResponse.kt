package com.example.fishsnap.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class ApiResponse<T>(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T
)