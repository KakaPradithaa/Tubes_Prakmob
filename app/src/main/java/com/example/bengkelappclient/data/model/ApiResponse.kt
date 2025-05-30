// data/model/ApiResponse.kt
package com.example.bengkelappclient.data.model

// Untuk respons yang mengembalikan list
data class ListApiResponse<T>(
    val status: Boolean?, // Atau field lain yang menandakan sukses/gagal
    val message: String?,
    val data: List<T>?,
    val errors: Map<String, List<String>>? // Untuk error validasi
)

// Untuk respons yang mengembalikan objek tunggal
data class ObjectApiResponse<T>(
    val status: Boolean?,
    val message: String?,
    val data: T?,
    val errors: Map<String, List<String>>?
)

// Untuk respons tanpa data (misal, delete sukses)
data class SimpleApiResponse(
    val status: Boolean?,
    val message: String?
)