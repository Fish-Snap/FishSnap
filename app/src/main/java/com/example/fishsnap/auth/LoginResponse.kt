package com.example.fishsnap.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("access_token")
    val token: String,

//    @SerializedName("user")
//    val user: User
) : Parcelable

//@Parcelize
//data class User(
//    @SerializedName("id")
//    val id: String,
//
//    @SerializedName("name")
//    val name: String,
//
//    @SerializedName("username")
//    val username: String,
//
//    @SerializedName("email")
//    val email: String,
//
//    @SerializedName("verified")
//    val verified: Boolean
//) : Parcelable