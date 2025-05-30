package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: Int,
    val user_id: Int,
    val name: String,
    val brand: String,
    val year: Int,
    val license_plate: String
)
