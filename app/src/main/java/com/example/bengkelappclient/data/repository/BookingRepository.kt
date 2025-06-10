package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferenceManager: UserPreferenceManager
    // Perhatikan: ServiceRepository tidak lagi dibutuhkan di sini
) {

    /**
     * Fungsi cerdas ini mengambil data booking untuk admin ATAU user biasa.
     * Data detail seperti vehicle dan service sudah termasuk di dalamnya (tidak ada N+1 call).
     */
    suspend fun getBookingDetails(): Result<List<BookingDetails>> {
        return try {
            // Ambil role user dari DataStore/Preferences
            val userRole = userPreferenceManager.userRole.first() ?: "user" // Sesuaikan 'userRole' dengan nama flow Anda

            // 1. Panggil endpoint yang BENAR berdasarkan role user
            val bookingsResponse = if (userRole.equals("admin", ignoreCase = true)) {
                apiService.getAllBookingsForAdmin() // Panggil fungsi untuk admin
            } else {
                apiService.getMyBookings() // Panggil fungsi untuk user biasa
            }

            // 2. Proses response dari satu panggilan API tersebut
            if (bookingsResponse.isSuccessful) {
                val bookings = bookingsResponse.body() ?: emptyList()

                // 3. Transformasi data. TIDAK PERLU PANGGILAN API LAGI!
                // Vehicle dan Service sudah ada di dalam setiap objek booking.
                val bookingDetailsList = bookings.mapNotNull { booking ->
                    // Cukup pastikan data vehicle tidak null untuk ditampilkan
                    if (booking.vehicle != null) {
                        BookingDetails(
                            booking = booking,
                            vehicle = booking.vehicle,
                            service = booking.service // service juga sudah ada di dalam booking
                        )
                    } else {
                        // Jika karena suatu hal data vehicle tidak ada, lewati item ini
                        null
                    }
                }
                Result.success(bookingDetailsList)
            } else {
                // Tangani jika response API tidak sukses
                Result.failure(Exception("API Error: ${bookingsResponse.code()} - ${bookingsResponse.message()}"))
            }
        } catch (e: Exception) {
            // Tangani exception lain seperti tidak ada koneksi internet
            Result.failure(e)
        }
    }
}