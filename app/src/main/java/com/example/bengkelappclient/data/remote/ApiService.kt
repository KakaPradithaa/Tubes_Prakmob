package com.example.bengkelappclient.data.remote

import com.example.bengkelappclient.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // =======================================================
    // ===               ENDPOINT AUTENTIKASI              ===
    // =======================================================

    @POST("register")
    suspend fun registerUser(@Body registrationData: Map<String, String>): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body loginData: Map<String, String>): Response<AuthResponse>

    @POST("logout")
    suspend fun logoutUser(): Response<SimpleApiResponse>

    @GET("user")
    suspend fun getUserProfile(): Response<AuthResponse>


    // =======================================================
    // ===            ENDPOINT PUBLIK / BERSAMA            ===
    // =======================================================

    /**
     * [PUBLIK] Mendapatkan semua layanan yang tersedia.
     */
    @GET("services")
    suspend fun getAllServices(): Response<List<Service>>

    /**
     * [PUBLIK] Mendapatkan semua jadwal operasional.
     */
    @GET("schedules")
    suspend fun getAllSchedules(): Response<List<Schedule>>


    // =======================================================
    // ===           ENDPOINT KHUSUS USER BIASA            ===
    // =======================================================

    // --- Manajemen Kendaraan User ---
    @GET("vehicles")
    suspend fun getMyVehicles(): Response<List<Vehicle>>

    @POST("vehicles")
    suspend fun createVehicle(@Body vehicle: Vehicle): Response<Vehicle>

    // --- Manajemen Booking User ---
    @GET("my-bookings")
    suspend fun getMyBookings(): Response<List<Booking>>

    @POST("bookings")
    suspend fun createBooking(@Body booking: Booking): Response<Booking>

    @GET("bookings/{id}")
    suspend fun getBookingById(@Path("id") bookingId: Int): Response<Booking>


    // =======================================================
    // ===            ENDPOINT KHUSUS ADMIN                ===
    // =======================================================

    // --- Manajemen Booking oleh Admin ---
    @GET("admin/bookings")
    suspend fun getAllBookingsForAdmin(): Response<List<Booking>>

    @PATCH("admin/bookings/{id}/status")
    suspend fun updateBookingStatus(
        @Path("id") bookingId: Int,
        @Body payload: StatusUpdatePayload
    ): Response<UpdateBookingResponse>

    @DELETE("admin/bookings/{id}")
    suspend fun deleteBookingByAdmin(@Path("id") bookingId: Int): Response<Unit>

    // --- Manajemen Jadwal oleh Admin ---
    @POST("admin/schedules")
    suspend fun createScheduleByAdmin(@Body schedule: Schedule): Response<Schedule>

    @PUT("admin/schedules/{id}")
    suspend fun updateScheduleByAdmin(@Path("id") scheduleId: Int, @Body schedule: Schedule): Response<Schedule>

    @DELETE("admin/schedules/{id}")
    suspend fun deleteScheduleByAdmin(@Path("id") scheduleId: Int): Response<Unit>

    // --- Manajemen Layanan (Service) oleh Admin ---
    @Multipart
    @POST("admin/services")
    suspend fun createServiceByAdmin(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Service>

    // Untuk update service dengan gambar, API Laravel butuh _method=PUT
    @Multipart
    @POST("admin/services/{id}")
    suspend fun updateServiceByAdmin(
        @Path("id") serviceId: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("_method") method: RequestBody // <-- HAPUS NILAI DEFAULT DARI SINI
    ): Response<Service>

    @DELETE("admin/services/{id}")
    suspend fun deleteServiceByAdmin(@Path("id") serviceId: Int): Response<Unit>
}