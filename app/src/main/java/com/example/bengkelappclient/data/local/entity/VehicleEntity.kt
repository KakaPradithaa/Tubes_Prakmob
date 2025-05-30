// data/local/entity/VehicleEntity.kt
package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bengkelappclient.data.local.entity.CustomerEntity

@Entity(
    tableName = "vehicles_table",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,       // Entitas parent
            parentColumns = ["id"],               // Kolom primary key di parent (CustomerEntity)
            childColumns = ["customerId"],        // Kolom foreign key di child ini (VehicleEntity)
            onDelete = ForeignKey.CASCADE,        // Jika customer dihapus, semua kendaraannya juga dihapus
            onUpdate = ForeignKey.CASCADE         // Jika id customer diupdate, customerId di vehicle juga update
        )
    ],
    indices = [
        Index(value = ["customerId"]),           // Index untuk optimasi query berdasarkan customerId
        Index(value = ["licensePlate"], unique = true) // Plat nomor harus unik
    ]
)
data class VehicleEntity(
    @PrimaryKey val id: Int,                     // Biasanya ID dari server
    val customerId: Int,                         // Foreign key ke CustomerEntity
    val licensePlate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val createdAt: String?,
    val updatedAt: String?
    )

