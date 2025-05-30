// data/model/ServiceOrder.kt
package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceOrder(
    val id: Int,
    @SerializedName("vehicle_id")
    val vehicleId: Int,
    val description: String,
    @SerializedName("entry_date")
    val entryDate: String, // Simpan sebagai String, konversi ke Date jika perlu di UI
    val status: String,
    @SerializedName("estimated_completion_date")
    val estimatedCompletionDate: String?,
    @SerializedName("actual_completion_date")
    val actualCompletionDate: String?,
    val notes: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    // val vehicle: Vehicle? = null // Jika API eager load vehicle
) : Parcelable