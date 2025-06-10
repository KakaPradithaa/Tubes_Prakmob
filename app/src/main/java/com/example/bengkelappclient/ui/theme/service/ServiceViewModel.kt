package com.example.bengkelappclient.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.repository.ServiceRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    // 1. LiveData untuk menampilkan daftar service utama
    private val _serviceList = MutableLiveData<Resource<List<Service>>>()
    val serviceList: LiveData<Resource<List<Service>>> = _serviceList

    // 2. LiveData untuk hasil operasi (Create, Update, Delete)
    // Dibungkus Event agar pesan/toast hanya muncul sekali
    private val _operationResult = MutableLiveData<Event<Resource<String>>>()
    val operationResult: LiveData<Event<Resource<String>>> = _operationResult

    init {
        fetchAllServices()
    }

    fun fetchAllServices() {
        viewModelScope.launch {
            _serviceList.postValue(Resource.Loading())
            // Langsung assign hasilnya, karena repository sudah mengembalikan Resource
            val result = serviceRepository.getAllServices()
            _serviceList.postValue(result)
        }
    }

    fun createService(name: String, description: String, price: String, imageFile: File?) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            val result = serviceRepository.createService(name, description, price, imageFile)
            when (result) {
                is Resource.Success -> {
                    _operationResult.postValue(Event(Resource.Success("Layanan baru berhasil ditambahkan!")))
                    fetchAllServices() // Refresh daftar setelah sukses
                }
                is Resource.Error -> {
                    _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal menambah layanan")))
                }
                else -> {}
            }
        }
    }

    fun updateService(serviceId: Int, name: String, description: String, price: String, imageFile: File?) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            val result = serviceRepository.updateService(serviceId, name, description, price, imageFile)
            when (result) {
                is Resource.Success -> {
                    _operationResult.postValue(Event(Resource.Success("Layanan berhasil diperbarui!")))
                    fetchAllServices() // Refresh daftar setelah sukses
                }
                is Resource.Error -> {
                    _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal memperbarui layanan")))
                }
                else -> {}
            }
        }
    }

    fun deleteService(serviceId: Int) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            val result = serviceRepository.deleteService(serviceId)
            when (result) {
                is Resource.Success -> {
                    _operationResult.postValue(Event(Resource.Success("Layanan berhasil dihapus!")))
                    fetchAllServices() // Refresh daftar setelah sukses
                }
                is Resource.Error -> {
                    _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal menghapus layanan")))
                }
                else -> {}
            }
        }
    }
}