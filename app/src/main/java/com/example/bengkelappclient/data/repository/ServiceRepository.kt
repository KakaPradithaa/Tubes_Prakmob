package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.remote.ApiService
import com.example.bengkelappclient.data.model.Service
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun addService(
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ): Response<Service> {
        return apiService.addService(name, description, price, image)
    }

    suspend fun getAllServices(): Response<List<Service>> {
        return apiService.getAllServices()
    }
}
