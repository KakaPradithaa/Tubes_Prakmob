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
     * Mengambil semua data jadwal dari server.
     */
    suspend fun getSchedules(): Resource<List<Schedule>> {
        return try {
            // Memanggil endpoint yang mengembalikan Response<List<Schedule>>
            val response = apiService.getAllSchedules()

            // Memeriksa apakah respons dari server berhasil (kode 2xx)
            if (response.isSuccessful) {
                // Mengambil data dari body dan memeriksa jika tidak null
                val schedules = response.body()
                if (schedules != null) {
                    Resource.Success(schedules)
                } else {
                    Resource.Error("Data jadwal tidak ditemukan.")
                }
            } else {
                // Menangani error dari server (kode 4xx atau 5xx)
                Resource.Error("Gagal memuat jadwal: ${response.message()}")
            }
        } catch (e: Exception) {
            // Menangani error jaringan atau exception lainnya
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }

    /**
     * Memperbarui data jadwal di server.
     */
    suspend fun updateSchedule(schedule: Schedule): Resource<Unit> {
        return try {
            // Memanggil endpoint yang mengembalikan Response<Schedule>
            val response = apiService.updateSchedule(schedule.id, schedule)

            // Hanya perlu memeriksa apakah permintaan berhasil, tidak perlu body
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Gagal memperbarui jadwal: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan pada jaringan")
        }
    }
}
