package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.remote.ApiService
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.util.Resource // Pastikan Anda memiliki class Resource ini
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor(
    private val apiService: ApiService
    // serviceDao bisa tetap ada jika Anda menggunakan caching ke database lokal (Room)
) {

    /**
     * [PUBLIK] Mengambil semua data layanan dari server.
     */
    suspend fun getAllServices(): Resource<List<Service>> {
        return try {
            val response = apiService.getAllServices()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Gagal memuat layanan: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    // --- FUNGSI-FUNGSI DI BAWAH INI UNTUK ADMIN ---

    /**
     * [ADMIN] Membuat layanan baru.
     * Fungsi ini menerima data mentah dan mengubahnya menjadi RequestBody di sini.
     */
    suspend fun createService(
        name: String,
        description: String,
        price: String,
        imageFile: File?
    ): Resource<Service> {
        return try {
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            // Memanggil fungsi yang benar di ApiService
            val response = apiService.createServiceByAdmin(nameBody, descriptionBody, priceBody, imagePart)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error("Gagal menambah layanan: $errorMsg")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    /**
     * [ADMIN] Memperbarui layanan yang ada.
     */
    suspend fun updateService(
        serviceId: Int,
        name: String,
        description: String,
        price: String,
        imageFile: File?
    ): Resource<Service> {
        return try {
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageFile?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            // Membuat RequestBody untuk _method
            val methodBody = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())

            // Memanggil fungsi yang benar di ApiService
            val response = apiService.updateServiceByAdmin(serviceId, nameBody, descriptionBody, priceBody, imagePart, methodBody)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error("Gagal memperbarui layanan: $errorMsg")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    /**
     * [ADMIN] Menghapus sebuah layanan.
     */
    suspend fun deleteService(serviceId: Int): Resource<Unit> {
        return try {
            // Memanggil fungsi yang benar di ApiService
            val response = apiService.deleteServiceByAdmin(serviceId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Gagal menghapus layanan: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }
}