// data/model/Booking.kt
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
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("booking_time")
    val bookingTime: String,
    val status: String,
    val complaint: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Parcelable
