// data/model/Customer.kt
package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    val id: Int,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val address: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    // Jika API Anda mengembalikan vehicles saat mengambil customer (eager loading)
    // val vehicles: List<Vehicle>? = null
) : Parcelable