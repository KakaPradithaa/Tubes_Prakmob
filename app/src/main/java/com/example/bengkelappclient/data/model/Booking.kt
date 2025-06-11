package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("vehicle_id")
    val vehicleId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("schedule_id")
    val scheduleId: Int?,

    @SerializedName("booking_date")
    val booking_date: String, // format: "YYYY-MM-DD"

    @SerializedName("booking_time")
    val booking_time: String, // format: "HH:mm:ss"

    @SerializedName("status")
    val status: String,

    @SerializedName("complaint")
    val complaint: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    // =======================================================
    // --- TAMBAHAN: Untuk menampung data relasi dari API ---
    // =======================================================
    @SerializedName("user")
    val user: User?,

    @SerializedName("vehicle")
    val vehicle: Vehicle?,

    @SerializedName("service")
    val service: Service?,

    @SerializedName("schedule")
    val schedule: Schedule?

) : Parcelable