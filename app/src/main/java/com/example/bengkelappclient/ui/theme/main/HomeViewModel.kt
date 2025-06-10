// app/src/main/java/com/example/bengkelappclient/ui/theme/main/HomeViewModel.kt
package com.example.bengkelappclient.ui.theme.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _services = MutableLiveData<ServiceResult<List<Service>>>()
    val services: LiveData<ServiceResult<List<Service>>> = _services

    init {
        fetchAllServices()
    }

    fun fetchAllServices() {
        viewModelScope.launch {
            _services.value = ServiceResult.Loading
            try {
                val response = serviceRepository.getAllServices()
                if (response.isSuccessful) {
                    response.body()?.let { serviceList ->
                        _services.value = ServiceResult.Success(serviceList)
                    } ?: run {
                        _services.value = ServiceResult.Error(Exception("Isi respons kosong"))
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Terjadi kesalahan tidak diketahui"
                    _services.value = ServiceResult.Error(Exception(errorMessage), errorMessage)
                }
            } catch (e: Exception) {
                _services.value = ServiceResult.Error(e, e.message)
            }
        }
    }
}