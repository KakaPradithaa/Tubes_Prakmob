package com.example.bengkelappclient.data.model

import com.google.gson.annotations.SerializedName

// Untuk respons yang mengembalikan list
data class ListApiResponse<T>(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<T>?,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? // Untuk error validasi
)

// Untuk respons yang mengembalikan objek tunggal
data class ObjectApiResponse<T>(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("errors")
    val errors: Map<String, List<String>>?
)

// Untuk respons tanpa data (misal, delete atau update sukses)
data class SimpleApiResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?
)
