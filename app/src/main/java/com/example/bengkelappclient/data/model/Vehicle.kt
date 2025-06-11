package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("model")
    val name: String,
    val brand: String,
    val year: Int,
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Parcelable
