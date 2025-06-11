package com.example.bengkelappclient.data.model // Sesuaikan dengan package Anda

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk MENGIRIM body JSON saat update status
 * Contoh: { "status": "confirmed" }
 */
data class StatusUpdatePayload(
    @SerializedName("status")
    val status: String
)

/**
 * Data class untuk MENERIMA response JSON setelah berhasil update status
 * Contoh: { "message": "...", "booking": {...} }
 */
data class UpdateBookingResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("booking")
    val booking: Booking // Pastikan data class 'Booking' Anda sudah ada dan di-import
)