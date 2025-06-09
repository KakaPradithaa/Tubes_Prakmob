package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("vehicle_id")
    val vehicleId: Int,
    @SerializedName("service_id")
    val serviceId: Int,
    @SerializedName("schedule_id") // NEW: Add schedule_id as per problem statement
    val scheduleId: Int?, // Make it nullable as per API if not always present
    val booking_date: String, // format: "YYYY-MM-DD"
    val booking_time: String, // format: "HH:mm:ss"
    val status: String,
    val complaint: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Parcelable