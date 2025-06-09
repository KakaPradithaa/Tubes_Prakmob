package com.example.bengkelappclient.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    // Untuk hasil operasi Add
    private val _serviceResult = MutableLiveData<ServiceResult<Service>>()
    val serviceResult: LiveData<ServiceResult<Service>> = _serviceResult

    // Untuk hasil operasi Delete
    private val _deleteResult = MutableLiveData<ServiceResult<String>>()
    val deleteResult: LiveData<ServiceResult<String>> = _deleteResult

    // Untuk hasil operasi Update
    private val _updateResult = MutableLiveData<ServiceResult<String>>()
    val updateResult: LiveData<ServiceResult<String>> = _updateResult

    // Untuk mengambil semua services
    private val _allServices = MutableLiveData<ServiceResult<List<Service>>>()
    val allServices: LiveData<ServiceResult<List<Service>>> = _allServices

    fun addService(
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _serviceResult.value = ServiceResult.Loading
            try {
                val response = serviceRepository.addService(name, description, price, image)
                if (response.isSuccessful) {
                    response.body()?.let { service ->
                        _serviceResult.value = ServiceResult.Success(service)
                    } ?: run {
                        _serviceResult.value = ServiceResult.Error(Exception("Response body is null"))
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _serviceResult.value = ServiceResult.Error(Exception(errorMessage), errorMessage)
                }
            } catch (e: Exception) {
                _serviceResult.value = ServiceResult.Error(e, e.message)
            }
        }
    }

    fun fetchAllServices() {
        viewModelScope.launch {
            _allServices.value = ServiceResult.Loading
            try {
                val response = serviceRepository.getAllServices()
                if (response.isSuccessful) {
                    response.body()?.let { services ->
                        _allServices.value = ServiceResult.Success(services)
                    } ?: run {
                        _allServices.value = ServiceResult.Error(Exception("Response body is null"))
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _allServices.value = ServiceResult.Error(Exception(errorMessage), errorMessage)
                }
            } catch (e: Exception) {
                _allServices.value = ServiceResult.Error(e, e.message)
            }
        }
    }

    fun deleteService(serviceId: Int) {
        viewModelScope.launch {
            _deleteResult.value = ServiceResult.Loading
            try {
                val response = serviceRepository.deleteService(serviceId)
                if (response.isSuccessful) {
                    _deleteResult.value = ServiceResult.Success("Layanan berhasil dihapus")
                    fetchAllServices() // Refresh daftar setelah berhasil hapus
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gagal menghapus"
                    _deleteResult.value = ServiceResult.Error(Exception(errorMsg), errorMsg)
                }
            } catch (e: Exception) {
                _deleteResult.value = ServiceResult.Error(e, e.message)
            }
        }
    }

    fun updateService(
        serviceId: Int,
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _updateResult.value = ServiceResult.Loading
            try {
                val response = serviceRepository.updateService(serviceId, name, description, price, image)

                // --- LOGIKA YANG DIPERBAIKI ---
                if (response.isSuccessful) {
                    // Jika response sukses, kirim pesan String, bukan objek Service.
                    _updateResult.value = ServiceResult.Success("Layanan berhasil diperbarui")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gagal memperbarui"
                    _updateResult.value = ServiceResult.Error(Exception(errorMsg), errorMsg)
                }
                // -----------------------------

            } catch (e: Exception) {
                _updateResult.value = ServiceResult.Error(e, e.message)
            }
        }
    }
}