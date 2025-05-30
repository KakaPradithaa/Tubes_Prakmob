// data/model/AuthResponse.kt
package com.example.bengkelappclient.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val status: Boolean,
    val message: String,
    val user: User?, // Bisa nullable jika registrasi/login gagal di tahap awal
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?
)