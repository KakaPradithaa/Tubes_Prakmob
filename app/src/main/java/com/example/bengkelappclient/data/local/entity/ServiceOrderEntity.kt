// data/local/entity/ServiceOrderEntity.kt
package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bengkelappclient.data.local.entity.VehicleEntity

@Entity(
    tableName = "service_orders_table", // <--- UBAH MENJADI INI
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["vehicleId"])
    ]
)
data class ServiceOrderEntity(
    @PrimaryKey val id: Int,
    val vehicleId: Int,
    val description: String,
    val entryDate: String,
    val status: String,
    val estimatedCompletionDate: String?,
    val actualCompletionDate: String?,
    val notes: String?,
    val createdAt: String?, // Pastikan DTO dan API juga handle ini jika disimpan
    val updatedAt: String?  // Pastikan DTO dan API juga handle ini jika disimpan
)