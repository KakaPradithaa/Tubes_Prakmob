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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // LiveData untuk daftar layanan
    private val _services = MutableLiveData<Resource<List<Service>>>()
    val services: LiveData<Resource<List<Service>>> = _services

    // LiveData untuk nama pengguna
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        // Panggil refreshData() saat ViewModel pertama kali dibuat
        refreshData()
    }

    // --- FUNGSI BARU UNTUK DIPANGGIL DARI UI ---
    fun refreshData() {
        fetchAllServices()
        fetchUserName()
    }

    private fun fetchAllServices() {
        viewModelScope.launch {
            _services.value = Resource.Loading()
            try {
                val result = serviceRepository.getAllServices()
                _services.value = result // Langsung teruskan hasil dari repository
            } catch (e: Exception) {
                _services.value = Resource.Error("Gagal memuat layanan: ${e.message}")
            }
        }
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            try {
                // PERBAIKAN: Gunakan .collect untuk mendapatkan nilai dari Flow
                authRepository.getCurrentUserName().collect { name ->
                    _userName.value = name ?: "Pengguna"
                }
            } catch (e: Exception) {
                _userName.value = "Pengguna" // Nilai default jika terjadi error
            }
        }
    }
}
