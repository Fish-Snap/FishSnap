package com.example.fishsnap.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("profilePic") val profilePic: String?,
    @SerializedName("role") val role: String,
    @SerializedName("isVerifiedEmail") val isVerifiedEmail: Boolean,
    @SerializedName("codeVerify") val codeVerify: String?,
    @SerializedName("expiresCodeVerifyAt") val expiresCodeVerifyAt: String?,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) : Parcelable