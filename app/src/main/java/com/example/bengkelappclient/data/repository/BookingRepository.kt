package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.model.Booking
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferenceManager: UserPreferenceManager,
    private val serviceRepository: ServiceRepository
) {

    suspend fun getBookingDetailsForCurrentUser(): Result<List<BookingDetails>> {
        return try {
            val userId = userPreferenceManager.userId.first()
                ?: return Result.failure(Exception("User not logged in or user ID not found."))

            val bookingsResponse = apiService.getAllBookings(userId = userId)

            if (bookingsResponse.isSuccessful && bookingsResponse.body() != null) {
                val bookings = bookingsResponse.body() ?: emptyList()
                val bookingDetailsList = mutableListOf<BookingDetails>()

                for (booking in bookings) {
                    val vehicleResponse = apiService.getVehicleById(booking.vehicleId)
                    val serviceResponse = serviceRepository.getServiceById(booking.serviceId)

                    val vehicle = if (vehicleResponse.isSuccessful && vehicleResponse.body() != null) {
                        vehicleResponse.body()
                    } else {
                        println("Failed to fetch vehicle for booking ID: ${booking.id} - ${vehicleResponse.code()}")
                        null
                    }

                    val service = if (serviceResponse.isSuccessful && serviceResponse.body() != null) {
                        serviceResponse.body()
                    } else {
                        println("Failed to fetch service for booking ID: ${booking.id} - ${serviceResponse.code()}")
                        null
                    }

                    if (vehicle != null) {
                        bookingDetailsList.add(BookingDetails(booking, vehicle, service))
                    } else {
                        println("Skipping booking ${booking.id} due to missing vehicle data.")
                    }
                }
                Result.success(bookingDetailsList)
            } else {
                Result.failure(Exception("Failed to fetch bookings: ${bookingsResponse.code()} ${bookingsResponse.message()} - ${bookingsResponse.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}