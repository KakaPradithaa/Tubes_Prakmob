package com.example.bengkelappclient.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Service // Import model Service
import com.example.bengkelappclient.data.model.ServiceResult // Import ServiceResult
import com.example.bengkelappclient.data.repository.ServiceRepository // Import ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * ViewModel for managing service-related operations.
 * Uses Hilt for dependency injection to provide ServiceRepository.
 */
@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    // MutableLiveData to hold the result of service operations.
    // It's private to ensure only the ViewModel can modify its value.
    private val _serviceResult = MutableLiveData<ServiceResult<Service>>()

    // Public LiveData to expose the service result to the UI.
    // UI components can observe this LiveData for updates.
    val serviceResult: LiveData<ServiceResult<Service>> = _serviceResult

    /**
     * Adds a new service by calling the repository.
     * The result (Loading, Success, or Error) is posted to _serviceResult.
     *
     * @param name RequestBody for the service name.
     * @param description RequestBody for the service description.
     * @param price RequestBody for the service price.
     * @param image Optional MultipartBody.Part for the service image.
     */
    fun addService(
        name: RequestBody,
        description: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part?
    ) {
        // Launch a coroutine in the ViewModel's scope for background operations.
        viewModelScope.launch {
            // Post Loading state immediately.
            _serviceResult.value = ServiceResult.Loading
            try {
                // Call the repository to add the service.
                val response = serviceRepository.addService(name, description, price, image)
                if (response.isSuccessful) {
                    // If the API call is successful, post Success with the response body.
                    response.body()?.let { service ->
                        _serviceResult.value = ServiceResult.Success(service)
                    } ?: run {
                        // If body is null despite success, post an Error.
                        _serviceResult.value = ServiceResult.Error(Exception("Response body is null"))
                    }
                } else {
                    // If the API call is not successful, extract error message and post Error.
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _serviceResult.value = ServiceResult.Error(Exception(errorMessage), errorMessage)
                }
            } catch (e: Exception) {
                // Catch any exceptions during the network call and post Error.
                _serviceResult.value = ServiceResult.Error(e, e.message)
            }
        }
    }

    private val _allServices = MutableLiveData<ServiceResult<List<Service>>>()
    val allServices: LiveData<ServiceResult<List<Service>>> = _allServices

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


    // You might also want a function to reset the serviceResult state after an operation
    fun resetServiceResult() {
        _serviceResult.value = null // Or a specific initial state
    }

    // Add other service-related functions here, e.g., get all services, get service by ID, etc.
    // Example:
    // private val _allServices = MutableLiveData<ServiceResult<List<Service>>>()
    // val allServices: LiveData<ServiceResult<List<Service>>> = _allServices
    //
    // fun fetchAllServices() {
    //     viewModelScope.launch {
    //         _allServices.value = ServiceResult.Loading
    //         try {
    //             val response = serviceRepository.getAllServices()
    //             if (response.isSuccessful) {
    //                 response.body()?.let { services ->
    //                     _allServices.value = ServiceResult.Success(services)
    //                 } ?: run {
    //                     _allServices.value = ServiceResult.Error(Exception("Response body is null"))
    //                 }
    //             } else {
    //                 val errorMessage = response.errorBody()?.string() ?: "Unknown error"
    //                 _allServices.value = ServiceResult.Error(Exception(errorMessage), errorMessage)
    //             }
    //         } catch (e: Exception) {
    //             _allServices.value = ServiceResult.Error(e, e.message)
    //         }
    //     }
    // }
}
