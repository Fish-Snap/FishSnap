package com.example.fishsnap.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("password") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)