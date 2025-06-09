package com.example.bengkelappclient.data.remote

import com.example.bengkelappclient.data.model.*
import com.example.bengkelappclient.util.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Auth Endpoints ---
    @POST("register")
    suspend fun registerUser(@Body registrationData: Map<String, String>): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body loginData: Map<String, String>): Response<AuthResponse>

    @GET("user")
    suspend fun getUserProfile(): Response<AuthResponse>

    @POST("logout")
    suspend fun logoutUser(): Response<SimpleApiResponse>

    // --- Vehicle Endpoints ---
    @GET("vehicles")
    suspend fun getAllVehicles(@Query("user_id") userId: Int? = null): Response<List<Vehicle>>

    @POST("vehicles")
    suspend fun createVehicle(@Body vehicle: Vehicle): Response<Vehicle>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") vehicleId: Int): Response<Vehicle>

    @PUT("vehicles/{id}")
    suspend fun updateVehicle(@Path("id") vehicleId: Int, @Body vehicle: Vehicle): Response<Vehicle>

    @DELETE("vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") vehicleId: Int): Response<Unit>

    // --- Service Endpoints ---
    @GET("services")
    suspend fun getAllServices(): Response<List<Service>>

    @Multipart
    @POST("services")
    suspend fun addService(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part img: MultipartBody.Part? = null
    ): Response<Service>

    @GET("services/{id}")
    suspend fun getServiceById(@Path("id") serviceId: Int): Response<Service>

    @PUT("services/{id}")
    suspend fun updateService(@Path("id") serviceId: Int, @Body service: Service): Response<Service>

    @DELETE("services/{id}")
    suspend fun deleteService(@Path("id") serviceId: Int): Response<Unit>

    // --- Booking Endpoints ---
    @GET("bookings")
    suspend fun getAllBookings(@Query("user_id") userId: Int? = null): Response<List<Booking>>

    @POST("bookings")
    suspend fun createBooking(@Body booking: Booking): Response<Booking>

    @GET("bookings/{id}")
    suspend fun getBookingById(@Path("id") bookingId: Int): Response<Booking>

    @PUT("bookings/{id}")
    suspend fun updateBooking(@Path("id") bookingId: Int, @Body booking: Booking): Response<Booking>

    @DELETE("bookings/{id}")
    suspend fun deleteBooking(@Path("id") bookingId: Int): Response<Unit>

    // --- Schedule Endpoints ---
    @GET("schedules")
    suspend fun getAllSchedules(): Response<List<Schedule>>

    @POST("schedules")
    suspend fun createSchedule(@Body schedule: Schedule): Response<Schedule>

    @GET("schedules/{id}")
    suspend fun getScheduleById(@Path("id") scheduleId: Int): Response<Schedule>

    @PUT("schedules/{id}")
    suspend fun updateSchedule(@Path("id") scheduleId: Int, @Body schedule: Schedule): Response<Schedule>

    @DELETE("schedules/{id}")
    suspend fun deleteSchedule(@Path("id") scheduleId: Int): Response<Unit>


    //-- Admin Endpoints--

    @Multipart
    @POST("services/{id}")
    suspend fun updateService(
        @Path("id") serviceId: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("_method") method: RequestBody
    ): Response<Unit>
}
