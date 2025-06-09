package com.example.bengkelappclient.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bengkelappclient.data.local.entity.BookingEntity
import com.example.bengkelappclient.data.local.entity.VehicleEntity

data class BookingWithVehicle(
    @Embedded val booking: BookingEntity,
    @Relation(
        parentColumn = "vehicle_id",
        entityColumn = "id"
    )
    val vehicle: VehicleEntity
)