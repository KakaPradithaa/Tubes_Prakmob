// data/model/VehicleDao.kt
package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val id: Int,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("license_plate")
    val licensePlate: String,
    val brand: String,
    val model: String,
    val year: Int,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    // val customer: Customer? = null, // Jika API eager load customer
    // val serviceOrders: List<ServiceOrder>? = null // Jika API eager load service orders
) : Parcelable