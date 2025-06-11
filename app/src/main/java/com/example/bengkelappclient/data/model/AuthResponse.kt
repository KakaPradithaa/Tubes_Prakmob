// data/model/AuthResponse.kt
package com.example.bengkelappclient.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: User
)


data class UserData(
    @SerializedName("access_token")
    val token: String,
    val name: String,
    val email: String,
    val role: String
)