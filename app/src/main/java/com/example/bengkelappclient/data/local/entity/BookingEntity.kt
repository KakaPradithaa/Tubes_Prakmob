package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: Int,
    val user_id: Int,
    val vehicle_id: Int,
    val service_id: Int,
    val schedule_id: Int,
    val booking_date: String, // format: "YYYY-MM-DD"
    val booking_time: String, // format: "HH:mm:ss"
    val status: String,
    val complaint: String?
)
