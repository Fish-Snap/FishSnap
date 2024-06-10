package com.example.fishsnap.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ApiResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: @RawValue Any?
) : Parcelable