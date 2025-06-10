package com.example.bengkelappclient.ui.theme.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.repository.AuthRepository
import com.example.bengkelappclient.data.repository.ServiceRepository
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authRepository: AuthRepository // DIUBAH: Suntikkan AuthRepository
) : ViewModel() {

    // LiveData untuk daftar layanan
    private val _services = MutableLiveData<Resource<List<Service>>>()
    val services: LiveData<Resource<List<Service>>> = _services

    // BARU: LiveData untuk nama pengguna
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        fetchAllServices()
        fetchUserName() // BARU: Panggil fungsi untuk mengambil nama user
    }

    fun fetchAllServices() {
        viewModelScope.launch {
            _services.value = Resource.Loading()
            when (val result = serviceRepository.getAllServices()) {
                is Resource.Success -> {
                    _services.value = Resource.Success(result.data!!)
                }
                is Resource.Error -> {
                    _services.value = Resource.Error(result.message ?: "Terjadi kesalahan")
                }
                else -> {} // Loading state
            }
        }
    }

    // BARU: Fungsi untuk mengambil nama pengguna dari DataStore melalui Repository
    private fun fetchUserName() {
        viewModelScope.launch {
            authRepository.getCurrentUserName().collect { name ->
                // Jika nama null (misal, baru login), tampilkan "Pengguna"
                _userName.value = name ?: "Pengguna"
            }
        }
    }
}