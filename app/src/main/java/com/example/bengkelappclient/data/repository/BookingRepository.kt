package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.model.StatusUpdatePayload
import com.example.bengkelappclient.data.model.UpdateBookingResponse // Pastikan import ini
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import com.example.bengkelappclient.data.model.Booking
import com.example.bengkelappclient.util.Resource // Tambahkan import ini


@Singleton
class BookingRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferenceManager: UserPreferenceManager
) {

    /**
     * Fungsi cerdas ini mengambil data booking untuk admin ATAU user biasa.
     * Data detail seperti vehicle dan service sudah termasuk di dalamnya (tidak ada N+1 call).
     */
    suspend fun getBookingDetails(): Result<List<BookingDetails>> {
        return try {
            // Ambil role user dari DataStore/Preferences
            val userRole = userPreferenceManager.userRole.first() ?: "user"

            // 1. Panggil endpoint yang BENAR berdasarkan role user
            val bookingsResponse = if (userRole.equals("admin", ignoreCase = true)) {
                apiService.getAllBookingsForAdmin()
            } else {
                apiService.getMyBookings()
            }

            // 2. Proses response dari satu panggilan API tersebut
            if (bookingsResponse.isSuccessful) {
                val bookings = bookingsResponse.body() ?: emptyList()

                // 3. Transformasi data. TIDAK PERLU PANGGILAN API LAGI!
                // Vehicle dan Service sudah ada di dalam setiap objek booking.
                val bookingDetailsList = bookings.mapNotNull { booking ->
                    // Cukup pastikan data vehicle tidak null untuk ditampilkan
                    if (booking.vehicle != null && booking.service != null) { // Pastikan service juga tidak null
                        BookingDetails(
                            booking = booking,
                            vehicle = booking.vehicle,
                            service = booking.service
                        )
                    } else {
                        // Jika karena suatu hal data vehicle atau service tidak ada, lewati item ini
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

    /**
     * [ADMIN] Memperbarui status booking.
     */
    suspend fun updateBookingStatus(bookingId: Int, payload: StatusUpdatePayload): Result<UpdateBookingResponse> {
        return try {
            val response = apiService.updateBookingStatus(bookingId, payload)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal update status: ${response.code()} - ${response.message()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(booking: Booking): Resource<Booking> { // Menggunakan Resource
        return try {
            val userId = userPreferenceManager.userId.first() // Ambil userId dari DataStore
            if (userId == null) {
                return Resource.Error("User ID tidak ditemukan. Harap login ulang.")
            }

            // Buat objek Booking baru dengan userId yang diambil dari DataStore.
            // Pastikan ID diset 0 atau nilai default karena akan di-generate oleh backend.
            val bookingWithUserId = booking.copy(userId = userId, id = 0)

            val response = apiService.createBooking(bookingWithUserId) // Panggil API service
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error("Gagal membuat booking: ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi kesalahan jaringan.")
        }
    }
}