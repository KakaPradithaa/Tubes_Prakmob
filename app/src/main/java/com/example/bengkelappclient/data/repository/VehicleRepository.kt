package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.model.Vehicle
import com.example.bengkelappclient.data.remote.ApiService
import com.example.bengkelappclient.util.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferenceManager: UserPreferenceManager // Diperlukan untuk mengambil userId
) {
    // Fungsi untuk membuat kendaraan baru
    suspend fun createVehicle(vehicle: Vehicle): Resource<Vehicle> {
        return try {
            val userId = userPreferenceManager.userId.first() // Ambil userId dari DataStore
            if (userId == null) {
                return Resource.Error("User ID tidak ditemukan. Harap login ulang.")
            }

            // Buat objek Vehicle baru dengan userId yang diambil
            // Karena Vehicle.kt Anda memiliki userId sebagai non-nullable,
            // kita akan menggunakan copy untuk memastikan userId terisi dari DataStore.
            val vehicleWithUserId = vehicle.copy(userId = userId)

            val response = apiService.createVehicle(vehicleWithUserId) // Mengirim Vehicle ke API
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Gagal menambah kendaraan: ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan jaringan.")
        }
    }

    // Fungsi lain untuk mendapatkan kendaraan (misalnya daftar kendaraan user)
    suspend fun getMyVehicles(): Resource<List<Vehicle>> {
        return try {
            val response = apiService.getMyVehicles()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Gagal memuat daftar kendaraan: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan jaringan.")
        }
    }

    // Tambahkan fungsi untuk update dan delete jika diperlukan
    suspend fun updateVehicle(vehicleId: Int, vehicle: Vehicle): Resource<Vehicle> {
        return try {
            val response = apiService.updateVehicle(vehicleId, vehicle)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Gagal memperbarui kendaraan: ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan jaringan.")
        }
    }

    suspend fun deleteVehicle(vehicleId: Int): Resource<Unit> {
        return try {
            val response = apiService.deleteVehicle(vehicleId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Gagal menghapus kendaraan: ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan jaringan.")
        }
    }
}