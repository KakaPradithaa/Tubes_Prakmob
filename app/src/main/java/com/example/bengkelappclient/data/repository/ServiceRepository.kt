package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.local.dao.ServiceDao
import com.example.bengkelappclient.data.remote.ApiService
import com.example.bengkelappclient.data.model.Service
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull // <-- TAMBAHKAN IMPORT INI
import okhttp3.RequestBody.Companion.toRequestBody // <-- TAMBAHKAN IMPORT INI

class ServiceRepository @Inject constructor(
    private val apiService: ApiService,
    private val serviceDao: ServiceDao
) {
    suspend fun addService(
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ): Response<Service> {
        return apiService.addService(name, description, price, image)
    }

    suspend fun deleteService(serviceId: Int): Response<Unit> {
        return apiService.deleteService(serviceId)
    }

    suspend fun updateService(
        serviceId: Int,
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ): Response<Unit> {
        // Trik untuk mengirim method PUT melalui POST jika backend Anda memerlukannya
        val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
        return apiService.updateService(serviceId, name, description, price, image, method)
    }

    suspend fun getAllServices(): Response<List<Service>> {
        return apiService.getAllServices()
    }
}