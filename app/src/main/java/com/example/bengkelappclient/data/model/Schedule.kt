// data/model/Schedule.kt
package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val id: Int,
    val day: String,
    @SerializedName("open_time")
    val openTime: String,
    @SerializedName("close_time")
    val closeTime: String,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Parcelable
