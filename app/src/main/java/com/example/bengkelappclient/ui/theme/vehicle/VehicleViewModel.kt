package com.example.bengkelappclient.ui.theme.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Vehicle
import com.example.bengkelappclient.data.repository.VehicleRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    // LiveData untuk hasil operasi penambahan/update/delete kendaraan
    private val _operationResult = MutableLiveData<Event<Resource<String>>>()
    val operationResult: LiveData<Event<Resource<String>>> = _operationResult

    // LiveData untuk daftar kendaraan (jika Anda ingin menampilkan daftar kendaraan di activity ini juga)
    private val _vehicles = MutableLiveData<Resource<List<Vehicle>>>()
    val vehicles: LiveData<Resource<List<Vehicle>>> = _vehicles

    // Fungsi untuk membuat kendaraan baru
    fun createVehicle(brand: String, model: String, year: String, licensePlate: String) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            try {
                val vehicle = Vehicle( // ID diisi 0 atau nilai dummy karena akan diabaikan oleh backend
                    id = 0,
                    userId = 0, // Akan diisi dari DataStore di Repository
                    name = model,
                    brand = brand,
                    year = year.toInt(),
                    licensePlate = licensePlate,
                    createdAt = null,
                    updatedAt = null
                )
                val result = vehicleRepository.createVehicle(vehicle)
                when (result) {
                    is Resource.Success -> {
                        _operationResult.postValue(Event(Resource.Success("Data kendaraan berhasil ditambahkan!")))
                        // Opsional: refresh daftar kendaraan setelah berhasil menambah
                        fetchMyVehicles()
                    }
                    is Resource.Error -> {
                        _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal menambah kendaraan")))
                    }
                    else -> {} // Loading state handled by initial postValue
                }
            } catch (e: Exception) {
                _operationResult.postValue(Event(Resource.Error(e.message ?: "Terjadi kesalahan saat menambah kendaraan.")))
            }
        }
    }

    // Fungsi untuk mengambil daftar kendaraan milik user
    fun fetchMyVehicles() {
        viewModelScope.launch {
            _vehicles.postValue(Resource.Loading())
            val result = vehicleRepository.getMyVehicles()
            _vehicles.postValue(result)
        }
    }

    // Fungsi untuk update kendaraan (contoh)
    fun updateVehicle(vehicleId: Int, brand: String, model: String, year: String, licensePlate: String) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            try {
                val vehicle = Vehicle(
                    id = vehicleId,
                    userId = 0, // userId akan diabaikan/diambil dari token di backend
                    name = model,
                    brand = brand,
                    year = year.toInt(),
                    licensePlate = licensePlate,
                    createdAt = null,
                    updatedAt = null
                )
                val result = vehicleRepository.updateVehicle(vehicleId, vehicle)
                when (result) {
                    is Resource.Success -> {
                        _operationResult.postValue(Event(Resource.Success("Data kendaraan berhasil diperbarui!")))
                        fetchMyVehicles()
                    }
                    is Resource.Error -> {
                        _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal memperbarui kendaraan")))
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _operationResult.postValue(Event(Resource.Error(e.message ?: "Terjadi kesalahan saat memperbarui kendaraan.")))
            }
        }
    }

    // Fungsi untuk delete kendaraan (contoh)
    fun deleteVehicle(vehicleId: Int) {
        viewModelScope.launch {
            _operationResult.postValue(Event(Resource.Loading()))
            val result = vehicleRepository.deleteVehicle(vehicleId)
            when (result) {
                is Resource.Success -> {
                    _operationResult.postValue(Event(Resource.Success("Data kendaraan berhasil dihapus!")))
                    fetchMyVehicles()
                }
                is Resource.Error -> {
                    _operationResult.postValue(Event(Resource.Error(result.message ?: "Gagal menghapus kendaraan")))
                }
                else -> {}
            }
        }
    }
}