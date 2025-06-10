package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.model.Schedule
import com.example.bengkelappclient.data.remote.ApiService
import com.example.bengkelappclient.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val apiService: ApiService
) {
    /**
     * [PUBLIK] Mengambil semua data jadwal dari server untuk ditampilkan ke semua user.
     */
    suspend fun getSchedules(): Resource<List<Schedule>> {
        return try {
            val response = apiService.getAllSchedules()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Gagal memuat jadwal: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    // --- FUNGSI-FUNGSI DI BAWAH INI UNTUK ADMIN ---

    /**
     * [ADMIN] Membuat jadwal baru.
     */
    suspend fun createSchedule(schedule: Schedule): Resource<Schedule> {
        return try {
            // Memanggil fungsi yang benar di ApiService
            val response = apiService.createScheduleByAdmin(schedule)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error("Gagal membuat jadwal: $errorMsg (Code: ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    /**
     * [ADMIN] Memperbarui data jadwal di server.
     */
    suspend fun updateSchedule(scheduleId: Int, schedule: Schedule): Resource<Schedule> {
        return try {
            // Memanggil fungsi yang BENAR di ApiService: updateScheduleByAdmin
            val response = apiService.updateScheduleByAdmin(scheduleId, schedule)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error("Gagal memperbarui jadwal: $errorMsg (Code: ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    /**
     * [ADMIN] Menghapus jadwal.
     */
    suspend fun deleteSchedule(scheduleId: Int): Resource<Unit> {
        return try {
            // Memanggil fungsi yang benar di ApiService
            val response = apiService.deleteScheduleByAdmin(scheduleId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error("Gagal menghapus jadwal: $errorMsg (Code: ${response.code()})")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }
}