package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Int, // harga dalam Rupiah
    val img: String?
)
