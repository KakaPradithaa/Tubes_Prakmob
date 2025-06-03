package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val token: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("role") // <--- TAMBAHKAN INI
    val role: String // <--- Tambahkan properti role
) : Parcelable
