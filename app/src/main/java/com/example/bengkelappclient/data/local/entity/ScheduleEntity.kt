package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val id: Int,
    val day: String, // e.g., "Monday"
    val open_time: String, // format: "HH:mm:ss"
    val close_time: String // format: "HH:mm:ss"
)
