package com.example.bengkelappclient.data.remote

import com.example.bengkelappclient.data.model.* // Pastikan ini benar
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


    // --- Customer Endpoints ---
    @GET("customers")
    suspend fun getAllCustomers(): Response<List<Customer>>

    @POST("customers")
    suspend fun createCustomer(@Body customer: Customer): Response<Customer>

    @GET("customers/{id}")
    suspend fun getCustomerById(@Path("id") customerId: Int): Response<Customer>

    @PUT("customers/{id}")
    suspend fun updateCustomer(@Path("id") customerId: Int, @Body customer: Customer): Response<Customer>

    @DELETE("customers/{id}")
    suspend fun deleteCustomer(@Path("id") customerId: Int): Response<Unit>


    // --- Vehicle Endpoints ---
    @GET("vehicles")
    suspend fun getAllVehicles(@Query("customer_id") customerId: Int? = null): Response<List<Vehicle>>

    @POST("vehicles")
    suspend fun createVehicle(@Body vehicle: Vehicle): Response<Vehicle>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") vehicleId: Int): Response<Vehicle>

    @PUT("vehicles/{id}")
    suspend fun updateVehicle(@Path("id") vehicleId: Int, @Body vehicle: Vehicle): Response<Vehicle>

    @DELETE("vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") vehicleId: Int): Response<Unit>


    // --- ServiceOrder Endpoints ---
    @GET("service-orders")
    suspend fun getAllServiceOrders(
        @Query("vehicle_id") vehicleId: Int? = null,
        @Query("status") status: String? = null
    ): Response<List<ServiceOrder>>

    @POST("service-orders")
    suspend fun createServiceOrder(@Body serviceOrder: ServiceOrder): Response<ServiceOrder>

    @GET("service-orders/{id}")
    suspend fun getServiceOrderById(@Path("id") serviceOrderId: Int): Response<ServiceOrder>

    @PUT("service-orders/{id}")
    suspend fun updateServiceOrder(@Path("id") serviceOrderId: Int, @Body serviceOrder: ServiceOrder): Response<ServiceOrder>

    @DELETE("service-orders/{id}")
    suspend fun deleteServiceOrder(@Path("id") serviceOrderId: Int): Response<Unit>
}